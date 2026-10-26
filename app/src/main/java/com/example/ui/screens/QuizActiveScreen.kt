package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAlert
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.model.LifelineType
import com.example.model.QuizMode
import com.example.model.UserStats
import com.example.ui.QuizSessionState
import com.example.ui.components.CountdownTimerBar
import com.example.ui.components.StrikesIndicator
import com.example.ui.theme.AmberWarning
import com.example.ui.theme.CrimsonStrike
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.GoldSecondary
import com.example.ui.theme.IndigoAccent
import com.example.ui.theme.QuizBorder
import com.example.ui.theme.QuizDarkBg
import com.example.ui.theme.QuizSurface
import com.example.ui.theme.QuizSurfaceVariant
import com.example.ui.theme.TextLightMuted
import com.example.ui.theme.TextLightPrimary
import com.example.ui.theme.TextLightSecondary

@Composable
fun QuizActiveScreen(
    session: QuizSessionState,
    userStats: UserStats,
    onOptionSelected: (Int) -> Unit,
    onUseSkip: () -> Unit,
    onWatchRewardedAd: () -> Unit,
    onWatchFullScreenAdReviveCategory: () -> Unit,
    onForfeitRound: () -> Unit,
    onExitQuiz: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler {
        onExitQuiz()
    }

    val isBn = userStats.language == "bn"
    val currentQuestion = session.questions.getOrNull(session.currentIndex)
    val questionText = if (isBn) currentQuestion?.questionBn ?: "" else currentQuestion?.questionEn ?: ""
    val optionsList = if (isBn) currentQuestion?.optionsBn ?: emptyList() else currentQuestion?.optionsEn ?: emptyList()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(QuizDarkBg)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 10.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Header: Close button, Mode Title, Progress & Coin pill
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onExitQuiz,
                    modifier = Modifier.testTag("exit_quiz_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Exit Quiz",
                        tint = TextLightSecondary
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = session.categoryTitle,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextLightPrimary
                    )
                    Text(
                        text = if (isBn) "প্রশ্ন ${session.currentIndex + 1} / ${session.totalQuestions}"
                        else "Question ${session.currentIndex + 1} of ${session.totalQuestions}",
                        fontSize = 12.sp,
                        color = GoldPrimary,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                // Coins earned in this round so far
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0xFF2C2208))
                        .border(1.dp, GoldPrimary, RoundedCornerShape(16.dp))
                        .padding(horizontal = 10.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.MonetizationOn,
                        contentDescription = null,
                        tint = GoldPrimary,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "+${session.coinsEarned}",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = GoldLight
                    )
                }
            }

            // Timer Bar & Timer countdown
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                CountdownTimerBar(
                    timeRemainingSeconds = session.timeRemainingSeconds,
                    maxTimeSeconds = session.maxTimeSeconds,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "${session.timeRemainingSeconds}s",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = when {
                        session.timeRemainingSeconds > 3 -> EmeraldSuccess
                        session.timeRemainingSeconds > 1 -> AmberWarning
                        else -> CrimsonStrike
                    }
                )
            }

            // Strikes Indicator ONLY in Bullet round; Missed Counter in Category round
            if (session.mode == QuizMode.BULLET) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(QuizSurface)
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    StrikesIndicator(strikes = session.strikes, maxStrikes = 3)
                    Text(
                        text = if (isBn) "ভুল উত্তরে ১ স্ট্রাইক (সর্বোচ্চ ৩ • ২০ কয়েন/প্রশ্ন)" else "3 Strikes Out (20 Coins / Q)",
                        fontSize = 11.sp,
                        color = CrimsonStrike,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            } else {
                // Category round: No strike rules, 5 coins per right answer, >6 missed disqualification rule
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Coin Reward & Missed counter Badge
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFF261C02))
                            .border(1.dp, GoldPrimary.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                            .padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(text = "🪙", fontSize = 13.sp)
                        Text(
                            text = if (isBn) "+৫ কয়েন" else "+5 Coins",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = GoldLight
                        )
                        Box(
                            modifier = Modifier
                                .size(4.dp)
                                .clip(CircleShape)
                                .background(TextLightMuted)
                        )
                        Text(
                            text = if (isBn) "মিস: ${session.missedCount}/6" else "Missed: ${session.missedCount}/6",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (session.missedCount >= 5) CrimsonStrike else AmberWarning
                        )
                    }

                    // Only Skip Lifeline button
                    LifelineButton(
                        title = "Skip",
                        count = userStats.skipCount,
                        enabled = !session.isAnswerSubmitted,
                        onClick = onUseSkip
                    )
                }
            }

            // Big Question Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("active_question_card"),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = QuizSurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, QuizBorder),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = questionText,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextLightPrimary,
                        lineHeight = 24.sp
                    )
                }
            }

            // 4 Options Grid/List
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                val optionPrefixes = listOf("A", "B", "C", "D")

                optionsList.forEachIndexed { index, optionText ->
                    val isSelected = session.selectedOptionIndex == index
                    val isCorrect = index == currentQuestion?.correctIndex

                    QuizOptionButton(
                        prefix = optionPrefixes.getOrElse(index) { "$index" },
                        text = optionText,
                        isSelected = isSelected,
                        isCorrect = isCorrect,
                        isSubmitted = session.isAnswerSubmitted,
                        isEliminated = false,
                        onClick = { onOptionSelected(index) }
                    )
                }
            }
        }

        // STRIKE-OUT MODAL (For Bullet Quiz at 3 Strikes)
        if (session.isStrikedOut) {
            Dialog(onDismissRequest = { /* Cannot dismiss without choice */ }) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .testTag("strike_out_modal"),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = QuizSurfaceVariant),
                    border = androidx.compose.foundation.BorderStroke(1.5.dp, CrimsonStrike)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(22.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Text(text = "❌ ❌ ❌", fontSize = 28.sp)
                        Text(
                            text = if (isBn) "স্ট্রাইক আউট!" else "STRIKE OUT!",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = CrimsonStrike
                        )
                        Text(
                            text = if (isBn)
                                "আপনি ৩টি ভুল উত্তর দিয়েছেন। রিওয়ার্ডেড ভিডিও দেখে স্ট্রাইক শূন্য করে খেলা চালিয়ে যান অথবা রাউন্ড ত্যাগ করুন।"
                            else
                                "You reached 3 strikes. Watch a sponsored rewarded ad to reset strikes to 0 and save your coins, or forfeit this round.",
                            fontSize = 13.sp,
                            color = TextLightSecondary,
                            textAlign = TextAlign.Center,
                            lineHeight = 18.sp
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Button(
                            onClick = onWatchRewardedAd,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .testTag("revive_ad_button"),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = EmeraldSuccess,
                                contentColor = Color.White
                            )
                        ) {
                            Text(
                                text = if (isBn) "🎬 অ্যাড দেখুন ও পুনরুজ্জীবিত হন" else "🎬 WATCH AD TO REVIVE",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }

                        OutlinedButton(
                            onClick = onForfeitRound,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(44.dp)
                                .testTag("forfeit_round_button"),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = TextLightMuted
                            )
                        ) {
                            Text(
                                text = if (isBn) "রাউন্ড ত্যাগ করুন (০ কয়েন)" else "Forfeit Round (No Coins)",
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }
        }

        // CATEGORY DISQUALIFICATION MODAL (For Category Round when missed > 6)
        if (session.isCategoryDisqualified) {
            Dialog(onDismissRequest = { /* Must choose action */ }) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .testTag("category_disqualified_modal"),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = QuizSurfaceVariant),
                    border = androidx.compose.foundation.BorderStroke(1.5.dp, CrimsonStrike)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(22.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Text(text = "⚠️ 🚫 ⚠️", fontSize = 28.sp)
                        Text(
                            text = if (isBn) "আপনি ডিসকোয়ালিফাইড!" else "DISQUALIFIED!",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = CrimsonStrike
                        )
                        Text(
                            text = if (isBn)
                                "আপনি ৬টির বেশি প্রশ্নের উত্তর ভুল দিয়েছেন বা সময় শেষ হয়েছে (${session.missedCount}/6)। লাইফলাইন পেতে স্কিপ ব্যবহার করুন অথবা ফুল স্ক্রিন অ্যাড দেখুন।"
                            else
                                "You missed more than 6 questions in this round (${session.missedCount} missed). Gain a lifeline by using Skip or watching a Full Screen AD to revive!",
                            fontSize = 13.sp,
                            color = TextLightSecondary,
                            textAlign = TextAlign.Center,
                            lineHeight = 18.sp
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Button(
                            onClick = onWatchFullScreenAdReviveCategory,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .testTag("revive_fullscreen_ad_button"),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = EmeraldSuccess,
                                contentColor = Color.White
                            )
                        ) {
                            Text(
                                text = if (isBn) "🎬 ফুল স্ক্রিন অ্যাড দেখুন (পুনরুজ্জীবিত)" else "🎬 WATCH FULL SCREEN AD TO REVIVE",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }

                        Button(
                            onClick = onUseSkip,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .testTag("revive_skip_button"),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = CyanAccent,
                                contentColor = Color(0xFF0F172A)
                            )
                        ) {
                            Text(
                                text = if (isBn) "⚡ স্কিপ লাইফলাইন ব্যবহার করুন (${userStats.skipCount} বাকি)" else "⚡ USE SKIP LIFELINE (${userStats.skipCount} Left / 50¢)",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }

                        OutlinedButton(
                            onClick = onForfeitRound,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(44.dp)
                                .testTag("forfeit_category_button"),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = TextLightMuted
                            )
                        ) {
                            Text(
                                text = if (isBn) "রাউন্ড ত্যাগ করুন" else "Forfeit Round",
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }
        }

        // WATCHING REWARDED AD COUNTDOWN OVERLAY
        if (session.isWatchingAd) {
            Dialog(onDismissRequest = {}) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = QuizDarkBg),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CyanAccent)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        CircularProgressIndicator(
                            color = CyanAccent,
                            modifier = Modifier.size(52.dp),
                            strokeWidth = 4.dp
                        )
                        Text(
                            text = if (isBn) "স্পন্সরড ভিডিও চলছে..." else "Playing Sponsored Ad...",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextLightPrimary
                        )
                        Text(
                            text = "${session.adCountdown}s",
                            fontSize = 32.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = GoldPrimary
                        )
                        Text(
                            text = if (isBn) "সম্পূর্ণ দেখুন স্ট্রাইক শূন্য করতে" else "Watch until end to clear strikes",
                            fontSize = 12.sp,
                            color = TextLightMuted
                        )
                    }
                }
            }
        }

        // ROUND FINISHED SUMMARY DIALOG
        if (session.isRoundFinished) {
            Dialog(onDismissRequest = onExitQuiz) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .testTag("round_finished_dialog"),
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(containerColor = QuizSurface),
                    border = androidx.compose.foundation.BorderStroke(1.5.dp, GoldPrimary)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(22.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Text(text = "🏆", fontSize = 42.sp)
                        Text(
                            text = if (isBn) "রাউন্ড সম্পন্ন!" else "Round Completed!",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = GoldLight
                        )

                        // Coins Earned Banner
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(14.dp))
                                .background(
                                    Brush.horizontalGradient(
                                        listOf(Color(0xFF382903), Color(0xFF261A02))
                                    )
                                )
                                .border(1.dp, GoldPrimary, RoundedCornerShape(14.dp))
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = if (isBn) "মোট অর্জিত কয়েন" else "Total Coins Earned",
                                    fontSize = 12.sp,
                                    color = GoldLight
                                )
                                Text(
                                    text = "+${session.coinsEarned}",
                                    fontSize = 34.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = GoldPrimary
                                )
                                if (session.perfectBonusEarned) {
                                    Text(
                                        text = if (isBn) "⭐ +৫০ পারফেক্ট রাউন্ড বোনাস!" else "⭐ +50 Perfect Round Bonus Included!",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = EmeraldSuccess
                                    )
                                }
                            }
                        }

                        // Stats Grid
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "${session.correctCount}/${session.totalQuestions}",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextLightPrimary
                                )
                                Text(
                                    text = if (isBn) "সঠিক উত্তর" else "Correct",
                                    fontSize = 11.sp,
                                    color = TextLightMuted
                                )
                            }
                            val accuracy = if (session.totalQuestions > 0)
                                (session.correctCount * 100) / session.totalQuestions else 0
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "$accuracy%",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = CyanAccent
                                )
                                Text(
                                    text = if (isBn) "নির্ভুলতা" else "Accuracy",
                                    fontSize = 11.sp,
                                    color = TextLightMuted
                                )
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "${session.maxStreak}x",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = EmeraldSuccess
                                )
                                Text(
                                    text = if (isBn) "সর্বোচ্চ স্ট্রিক" else "Max Streak",
                                    fontSize = 11.sp,
                                    color = TextLightMuted
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Button(
                            onClick = onExitQuiz,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp)
                                .testTag("claim_and_exit_button"),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = GoldPrimary,
                                contentColor = Color(0xFF1E1500)
                            )
                        ) {
                            Text(
                                text = if (isBn) "কয়েন যোগ করুন ও ফিরুন" else "CLAIM COINS & RETURN",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LifelineButton(
    title: String,
    count: Int,
    enabled: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(if (enabled) QuizSurface else Color(0xFF1A2238))
            .border(1.dp, if (enabled) QuizBorder else Color(0xFF243050), RoundedCornerShape(12.dp))
            .clickable(enabled = enabled) { onClick() }
            .padding(horizontal = 10.dp, vertical = 6.dp)
            .testTag("lifeline_$title")
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = title,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = if (enabled) TextLightPrimary else TextLightMuted
            )
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(if (count > 0) CyanAccent else GoldPrimary)
                    .padding(horizontal = 5.dp, vertical = 1.dp)
            ) {
                Text(
                    text = if (count > 0) "$count" else "50¢",
                    fontSize = 9.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF0F172A)
                )
            }
        }
    }
}

@Composable
fun QuizOptionButton(
    prefix: String,
    text: String,
    isSelected: Boolean,
    isCorrect: Boolean,
    isSubmitted: Boolean,
    isEliminated: Boolean,
    onClick: () -> Unit
) {
    if (isEliminated) {
        // 50/50 removed option
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(Color(0xFF11172A))
                .border(1.dp, Color(0xFF1E293B), RoundedCornerShape(14.dp))
                .padding(14.dp)
        ) {
            Text(
                text = "$prefix.  - - - - -",
                fontSize = 14.sp,
                color = TextLightMuted.copy(alpha = 0.4f)
            )
        }
        return
    }

    val backgroundColor = when {
        isSubmitted && isSelected && isCorrect -> EmeraldSuccess.copy(alpha = 0.25f)
        isSubmitted && isSelected && !isCorrect -> CrimsonStrike.copy(alpha = 0.25f)
        isSelected -> GoldPrimary.copy(alpha = 0.15f)
        else -> QuizSurface
    }

    val borderColor = when {
        isSubmitted && isSelected && isCorrect -> EmeraldSuccess
        isSubmitted && isSelected && !isCorrect -> CrimsonStrike
        isSelected -> GoldPrimary
        else -> QuizBorder
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .clickable(enabled = !isSubmitted) { onClick() }
            .testTag("option_$prefix"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        border = androidx.compose.foundation.BorderStroke(1.2.dp, borderColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(30.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF1E293B)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = prefix,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextLightPrimary
                    )
                }

                Text(
                    text = text,
                    fontSize = 14.sp,
                    color = TextLightPrimary,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                )
            }

            if (isSubmitted && isSelected) {
                if (isCorrect) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Correct",
                        tint = EmeraldSuccess,
                        modifier = Modifier.size(20.dp)
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Wrong",
                        tint = CrimsonStrike,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}
