package com.example

import android.app.Application
import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.QuestionBank
import com.example.data.QuizRepository
import com.example.model.QuizMode
import com.example.ui.QuizViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

    @Test
    fun `read string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("QuizEarn", appName)
    }

    @Test
    fun `question bank has categories and questions`() {
        assertTrue(QuestionBank.categories.isNotEmpty())
        assertTrue(QuestionBank.allQuestions.size >= 10)

        val bulletQuestions = QuestionBank.getBulletQuestions(5)
        assertEquals(5, bulletQuestions.size)
        bulletQuestions.forEach { q ->
            assertNotNull(q.questionEn)
            assertNotNull(q.questionBn)
            assertEquals(4, q.optionsEn.size)
            assertEquals(4, q.optionsBn.size)
        }
    }

    @Test
    fun `bullet round gives 20 coins for correct and charges strike for wrong`() {
        val app = ApplicationProvider.getApplicationContext<Application>()
        val vm = QuizViewModel(app)

        vm.startBulletQuiz()
        val session = vm.session.value
        assertTrue(session.isPlaying)
        assertEquals(0, session.strikes)
        assertEquals(0, session.coinsEarned)

        val currentQ = session.questions[session.currentIndex]
        // Submit correct answer
        vm.submitAnswer(currentQ.correctIndex)
        val afterCorrect = vm.session.value
        assertEquals(20, afterCorrect.coinsEarned)
        assertEquals(0, afterCorrect.strikes)

        // Test wrong answer on a fresh bullet quiz
        val vm2 = QuizViewModel(app)
        vm2.startBulletQuiz()
        val currentQ2 = vm2.session.value.questions[vm2.session.value.currentIndex]
        val wrongIndex = if (currentQ2.correctIndex == 0) 1 else 0
        vm2.submitAnswer(wrongIndex)
        assertEquals(1, vm2.session.value.strikes)
        assertEquals(0, vm2.session.value.coinsEarned)
    }

    @Test
    fun `category round gives 5 coins for correct and no strikes for wrong`() {
        val app = ApplicationProvider.getApplicationContext<Application>()
        val vm = QuizViewModel(app)

        vm.startCategoryQuiz("science")
        val session = vm.session.value
        assertTrue(session.isPlaying)
        assertEquals(0, session.strikes)
        assertEquals(0, session.coinsEarned)

        val currentQ = session.questions[session.currentIndex]
        // Submit correct answer
        vm.submitAnswer(currentQ.correctIndex)
        val afterCorrect = vm.session.value
        assertEquals(5, afterCorrect.coinsEarned)
        assertEquals(0, afterCorrect.strikes)

        // Submit wrong answer: no strike charged in category round!
        val wrongIndex = if (currentQ.correctIndex == 0) 1 else 0
        vm.submitAnswer(wrongIndex)
        val afterWrong = vm.session.value
        assertEquals(0, afterWrong.strikes)
    }

    @Test
    fun `watch ad reward adds 20 coins to repository`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val repo = QuizRepository(context)

        val initialCoins = repo.userStats.value.coins
        repo.grantAdRewardCoins(20)
        assertEquals(initialCoins + 20, repo.userStats.value.coins)
    }

    @Test
    fun `question bank has the exact 6 user categories with emojis`() {
        val categories = QuestionBank.categories
        assertEquals(6, categories.size)
        val expectedTitles = listOf("General", "Science", "Technology", "Geography", "Sports", "Current Affairs")
        val expectedEmojis = listOf("🧠", "🔬", "💻", "🌍", "🏆", "🗞️")

        assertEquals(expectedTitles, categories.map { it.titleEn })
        assertEquals(expectedEmojis, categories.map { it.emoji })
    }

    @Test
    fun `category round disqualifies when user misses more than 6 questions`() {
        val app = ApplicationProvider.getApplicationContext<Application>()
        val vm = QuizViewModel(app)

        vm.startCategoryQuiz("science")
        val session = vm.session.value
        assertEquals(false, session.isCategoryDisqualified)
        assertEquals(0, session.missedCount)

        // Simulate 7 wrong answers / timeouts
        for (i in 1..7) {
            val q = vm.session.value.questions[vm.session.value.currentIndex]
            val wrong = if (q.correctIndex == 0) 1 else 0
            vm.submitAnswer(wrong)
            if (!vm.session.value.isCategoryDisqualified) {
                vm.goToNextQuestion()
            }
        }

        val afterSevenMisses = vm.session.value
        assertTrue(afterSevenMisses.missedCount > 6)
        assertTrue(afterSevenMisses.isCategoryDisqualified)

        // Revive with Skip lifeline
        val revived = vm.useSkip()
        assertTrue(revived)
        assertEquals(false, vm.session.value.isCategoryDisqualified)
        assertEquals(0, vm.session.value.missedCount)
    }
}
