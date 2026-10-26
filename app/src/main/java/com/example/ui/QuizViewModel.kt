package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.QuestionBank
import com.example.data.QuizRepository
import com.example.model.LifelineType
import com.example.model.QuizMode
import com.example.model.QuizQuestion
import com.example.model.RedeemReward
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

enum class NavigationTab {
    HOME,
    PLAY,
    REWARD,
    LEADERBOARD,
    PROFILE
}

data class QuizSessionState(
    val isPlaying: Boolean = false,
    val mode: QuizMode = QuizMode.BULLET,
    val categoryTitle: String = "Bullet Quiz",
    val questions: List<QuizQuestion> = emptyList(),
    val currentIndex: Int = 0,
    val totalQuestions: Int = 20,
    val timeRemainingSeconds: Int = 5,
    val maxTimeSeconds: Int = 5,
    val strikes: Int = 0, // Bullet quiz max 3
    val missedCount: Int = 0, // Category round missed questions (max 6 allowed)
    val isCategoryDisqualified: Boolean = false, // When missedCount > 6
    val streak: Int = 0,
    val maxStreak: Int = 0,
    val correctCount: Int = 0,
    val coinsEarned: Int = 0,
    val perfectBonusEarned: Boolean = false,
    val selectedOptionIndex: Int? = null,
    val isAnswerSubmitted: Boolean = false,
    val isStrikedOut: Boolean = false,
    val isWatchingAd: Boolean = false,
    val adCountdown: Int = 5,
    val isRoundFinished: Boolean = false
)

class QuizViewModel(application: Application) : AndroidViewModel(application) {

    val repository = QuizRepository(application)

    val userStats = repository.userStats
    val redeemRequests = repository.redeemRequests
    val activityLog = repository.activityLog

    private val _currentTab = MutableStateFlow(NavigationTab.HOME)
    val currentTab: StateFlow<NavigationTab> = _currentTab.asStateFlow()

    private val _session = MutableStateFlow(QuizSessionState())
    val session: StateFlow<QuizSessionState> = _session.asStateFlow()

    private val _selectedReward = MutableStateFlow<RedeemReward?>(null)
    val selectedReward: StateFlow<RedeemReward?> = _selectedReward.asStateFlow()

    private val _showAdminSheet = MutableStateFlow(false)
    val showAdminSheet: StateFlow<Boolean> = _showAdminSheet.asStateFlow()

    private val _isWatchingRewardAd = MutableStateFlow(false)
    val isWatchingRewardAd: StateFlow<Boolean> = _isWatchingRewardAd.asStateFlow()

    private val _rewardAdCountdown = MutableStateFlow(5)
    val rewardAdCountdown: StateFlow<Int> = _rewardAdCountdown.asStateFlow()

    private val _toastMessage = MutableStateFlow<String?>(null)
    val toastMessage: StateFlow<String?> = _toastMessage.asStateFlow()

    private var timerJob: Job? = null
    private var adTimerJob: Job? = null

    fun selectTab(tab: NavigationTab) {
        _currentTab.value = tab
    }

    fun setShowAdminSheet(show: Boolean) {
        _showAdminSheet.value = show
    }

    fun setSelectedReward(reward: RedeemReward?) {
        _selectedReward.value = reward
    }

    fun clearToast() {
        _toastMessage.value = null
    }

    fun showToast(msg: String) {
        _toastMessage.value = msg
    }

    fun toggleLanguage() {
        val current = userStats.value.language
        val next = if (current == "en") "bn" else "en"
        repository.setLanguage(next)
        showToast(if (next == "bn") "ভাষা পরিবর্তন করা হয়েছে: বাংলা" else "Language changed to English")
    }

    fun claimDailyStreak(): Boolean {
        val success = repository.checkAndClaimDailyStreak()
        if (success) {
            showToast("🎉 Daily Streak Claimed! +25 Coins Added")
        } else {
            showToast("Already claimed today! Come back tomorrow.")
        }
        return success
    }

    fun startBulletQuiz() {
        val questions = QuestionBank.getBulletQuestions(20)
        startQuizSession(QuizMode.BULLET, "Bullet Quiz", questions, maxTime = 5)
    }

    fun startCategoryQuiz(categoryId: String) {
        val cat = QuestionBank.categories.find { it.id == categoryId }
        val title = if (userStats.value.language == "bn") cat?.titleBn ?: "Category" else cat?.titleEn ?: "Category"
        val questions = QuestionBank.getQuestionsForCategory(categoryId, 20)
        startQuizSession(QuizMode.CATEGORY, title, questions, maxTime = 8)
    }

    private fun startQuizSession(
        mode: QuizMode,
        title: String,
        questions: List<QuizQuestion>,
        maxTime: Int
    ) {
        timerJob?.cancel()
        _session.value = QuizSessionState(
            isPlaying = true,
            mode = mode,
            categoryTitle = title,
            questions = questions,
            currentIndex = 0,
            totalQuestions = questions.size,
            timeRemainingSeconds = maxTime,
            maxTimeSeconds = maxTime,
            strikes = 0,
            missedCount = 0,
            isCategoryDisqualified = false,
            streak = 0,
            maxStreak = 0,
            correctCount = 0,
            coinsEarned = 0,
            selectedOptionIndex = null,
            isAnswerSubmitted = false,
            isStrikedOut = false,
            isWatchingAd = false,
            isRoundFinished = false
        )
        startTimer()
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (_session.value.timeRemainingSeconds > 0 && !_session.value.isAnswerSubmitted && !_session.value.isStrikedOut && !_session.value.isCategoryDisqualified) {
                delay(1000)
                val newTime = _session.value.timeRemainingSeconds - 1
                _session.value = _session.value.copy(timeRemainingSeconds = newTime)
            }
            if (_session.value.timeRemainingSeconds <= 0 && !_session.value.isAnswerSubmitted && !_session.value.isStrikedOut && !_session.value.isCategoryDisqualified) {
                handleTimeout()
            }
        }
    }

    private fun handleTimeout() {
        val curr = _session.value
        val isBullet = curr.mode == QuizMode.BULLET
        val newStrikes = if (isBullet) curr.strikes + 1 else 0 // Strike charge ONLY in bullet round!
        val newMissed = if (!isBullet) curr.missedCount + 1 else 0

        _session.value = curr.copy(
            isAnswerSubmitted = true,
            selectedOptionIndex = -1, // Timed out
            streak = 0,
            strikes = newStrikes,
            missedCount = newMissed
        )

        if (isBullet && newStrikes >= 3) {
            _session.value = _session.value.copy(isStrikedOut = true)
        } else if (!isBullet && newMissed > 6) {
            _session.value = _session.value.copy(isCategoryDisqualified = true)
        } else {
            viewModelScope.launch {
                delay(800) // times up auto next question
                goToNextQuestion()
            }
        }
    }

    fun submitAnswer(optionIndex: Int) {
        val curr = _session.value
        if (curr.isAnswerSubmitted || curr.isStrikedOut || curr.isCategoryDisqualified || !curr.isPlaying) return

        timerJob?.cancel()
        val currentQuestion = curr.questions.getOrNull(curr.currentIndex) ?: return
        val isCorrect = optionIndex == currentQuestion.correctIndex
        val isBullet = curr.mode == QuizMode.BULLET

        var newCorrect = curr.correctCount
        var newCoins = curr.coinsEarned
        var newStreak = curr.streak
        var newMaxStreak = curr.maxStreak
        var newStrikes = curr.strikes
        var newMissed = curr.missedCount

        if (isCorrect) {
            newCorrect++
            newStreak++
            newMaxStreak = maxOf(newMaxStreak, newStreak)

            if (isBullet) {
                newCoins += 20 // Per successful answer user gets 20 coins in bullet round!
            } else {
                newCoins += 5  // Every right answer user gets 5 coins in category rounds!
            }
        } else {
            newStreak = 0
            if (isBullet) {
                newStrikes++ // Strike charge ONLY in bullet round!
            } else {
                newMissed++ // Category missed counter (wrong answer or timeout)
            }
        }

        _session.value = curr.copy(
            isAnswerSubmitted = true,
            selectedOptionIndex = optionIndex,
            correctCount = newCorrect,
            coinsEarned = newCoins,
            streak = newStreak,
            maxStreak = newMaxStreak,
            strikes = newStrikes,
            missedCount = newMissed
        )

        if (isBullet && newStrikes >= 3) {
            _session.value = _session.value.copy(isStrikedOut = true)
        } else if (!isBullet && newMissed > 6) {
            _session.value = _session.value.copy(isCategoryDisqualified = true)
        } else {
            viewModelScope.launch {
                delay(800) // Brief delay to show chosen answer feedback, then next question if available
                goToNextQuestion()
            }
        }
    }

    internal fun goToNextQuestion() {
        val curr = _session.value
        val nextIndex = curr.currentIndex + 1

        if (nextIndex >= curr.questions.size) {
            finishRound()
        } else {
            _session.value = curr.copy(
                currentIndex = nextIndex,
                timeRemainingSeconds = curr.maxTimeSeconds,
                selectedOptionIndex = null,
                isAnswerSubmitted = false
            )
            startTimer()
        }
    }

    fun watchRewardedAd() {
        adTimerJob?.cancel()
        _session.value = _session.value.copy(isWatchingAd = true, adCountdown = 5)

        adTimerJob = viewModelScope.launch {
            for (i in 5 downTo 1) {
                _session.value = _session.value.copy(adCountdown = i)
                delay(1000)
            }
            // Ad completed! Revive strikes to 0 and advance to next question if available
            val nextIndex = _session.value.currentIndex + 1
            if (nextIndex >= _session.value.questions.size) {
                _session.value = _session.value.copy(
                    isWatchingAd = false,
                    isStrikedOut = false,
                    strikes = 0
                )
                finishRound()
            } else {
                _session.value = _session.value.copy(
                    isWatchingAd = false,
                    isStrikedOut = false,
                    strikes = 0,
                    currentIndex = nextIndex,
                    timeRemainingSeconds = _session.value.maxTimeSeconds,
                    selectedOptionIndex = null,
                    isAnswerSubmitted = false
                )
                showToast("✨ Revived! Strikes reset to 0. Next question ready!")
                startTimer()
            }
        }
    }

    fun watchFullScreenAdReviveCategory() {
        adTimerJob?.cancel()
        _session.value = _session.value.copy(isWatchingAd = true, adCountdown = 5)

        adTimerJob = viewModelScope.launch {
            for (i in 5 downTo 1) {
                _session.value = _session.value.copy(adCountdown = i)
                delay(1000)
            }
            // Full screen ad completed! Clear category missed count and advance to next question
            val nextIndex = _session.value.currentIndex + 1
            if (nextIndex >= _session.value.questions.size) {
                _session.value = _session.value.copy(
                    isWatchingAd = false,
                    isCategoryDisqualified = false,
                    missedCount = 0
                )
                finishRound()
            } else {
                _session.value = _session.value.copy(
                    isWatchingAd = false,
                    isCategoryDisqualified = false,
                    missedCount = 0,
                    currentIndex = nextIndex,
                    timeRemainingSeconds = _session.value.maxTimeSeconds,
                    selectedOptionIndex = null,
                    isAnswerSubmitted = false
                )
                showToast("✨ Revived by Full Screen Ad! Missed counter reset. Next question ready!")
                startTimer()
            }
        }
    }

    fun forfeitRound() {
        timerJob?.cancel()
        adTimerJob?.cancel()
        _session.value = _session.value.copy(
            isPlaying = false,
            isStrikedOut = false,
            isCategoryDisqualified = false,
            isWatchingAd = false
        )
        showToast("Round forfeited. No coins recorded.")
    }

    fun exitQuiz() {
        timerJob?.cancel()
        adTimerJob?.cancel()
        _session.value = QuizSessionState(isPlaying = false)
    }

    private fun finishRound() {
        timerJob?.cancel()
        val curr = _session.value
        var finalCoins = curr.coinsEarned
        var perfectBonus = false

        // Category round 20/20 perfect bonus +50 coins
        if (curr.mode == QuizMode.CATEGORY && curr.correctCount == curr.totalQuestions && curr.totalQuestions >= 5) {
            finalCoins += 50
            perfectBonus = true
        }

        repository.onQuizRoundCompleted(
            modeName = curr.categoryTitle,
            correctCount = curr.correctCount,
            totalQuestions = curr.totalQuestions,
            coinsEarned = finalCoins,
            maxStreakInRound = curr.maxStreak
        )

        _session.value = curr.copy(
            coinsEarned = finalCoins,
            perfectBonusEarned = perfectBonus,
            isRoundFinished = true
        )
    }

    fun useSkip(): Boolean {
        val curr = _session.value
        if (curr.mode == QuizMode.BULLET) return false
        // Allow skip if disqualified OR if currently playing and answer not submitted
        if (curr.isAnswerSubmitted && !curr.isCategoryDisqualified) return false

        val used = repository.useLifeline(LifelineType.SKIP)
        if (!used) {
            val bought = repository.buyLifeline(LifelineType.SKIP)
            if (!bought) {
                showToast("Not enough coins (Need 50)!")
                return false
            }
            repository.useLifeline(LifelineType.SKIP)
        }

        timerJob?.cancel()
        _session.value = curr.copy(
            isCategoryDisqualified = false,
            missedCount = 0
        )
        showToast("✨ Question Skipped & Lifeline Used!")
        goToNextQuestion()
        return true
    }

    fun requestRedeem(reward: RedeemReward, accountDetail: String): Boolean {
        if (accountDetail.isBlank()) {
            showToast("Please enter your account details")
            return false
        }
        val success = repository.requestRedemption(reward, accountDetail)
        if (success) {
            showToast("Redemption request submitted! Pending admin review.")
            _selectedReward.value = null
        } else {
            showToast("Insufficient coin balance for this reward.")
        }
        return success
    }

    fun adminDecideRedeem(requestId: String, approve: Boolean) {
        repository.adminDecideRedemption(requestId, approve)
        showToast(if (approve) "Request Approved & Marked Paid" else "Request Rejected & Coins Refunded")
    }

    fun grantDemoCoins(amount: Int) {
        repository.grantDemoCoins(amount)
        showToast("Granted +$amount test coins!")
    }

    fun watchRewardAdForCoins() {
        if (_isWatchingRewardAd.value) return
        _isWatchingRewardAd.value = true
        _rewardAdCountdown.value = 5

        viewModelScope.launch {
            for (i in 5 downTo 1) {
                _rewardAdCountdown.value = i
                delay(1000)
            }
            _isWatchingRewardAd.value = false
            repository.grantAdRewardCoins(20)
            showToast("🎉 Watched Ad! +20 Coins added to your wallet!")
        }
    }
}
