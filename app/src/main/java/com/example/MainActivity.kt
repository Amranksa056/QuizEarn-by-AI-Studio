package com.example

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.NavigationTab
import com.example.ui.QuizViewModel
import com.example.ui.components.QuizBottomNavigationBar
import com.example.ui.components.QuizTopBar
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.LeaderboardScreen
import com.example.ui.screens.PlayScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.QuizActiveScreen
import com.example.ui.screens.RewardScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.QuizDarkBg

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                QuizEarnApp()
            }
        }
    }
}

@Composable
fun QuizEarnApp(viewModel: QuizViewModel = viewModel()) {
    val context = LocalContext.current
    val currentTab by viewModel.currentTab.collectAsState()
    val session by viewModel.session.collectAsState()
    val userStats by viewModel.userStats.collectAsState()
    val redeemRequests by viewModel.redeemRequests.collectAsState()
    val activityLog by viewModel.activityLog.collectAsState()
    val selectedReward by viewModel.selectedReward.collectAsState()
    val showAdminSheet by viewModel.showAdminSheet.collectAsState()
    val isWatchingRewardAd by viewModel.isWatchingRewardAd.collectAsState()
    val rewardAdCountdown by viewModel.rewardAdCountdown.collectAsState()
    val toastMessage by viewModel.toastMessage.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(toastMessage) {
        toastMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            viewModel.clearToast()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(QuizDarkBg)
            .windowInsetsPadding(WindowInsets.statusBars)
    ) {
        if (session.isPlaying) {
            QuizActiveScreen(
                session = session,
                userStats = userStats,
                onOptionSelected = { viewModel.submitAnswer(it) },
                onUseSkip = { viewModel.useSkip() },
                onWatchRewardedAd = { viewModel.watchRewardedAd() },
                onWatchFullScreenAdReviveCategory = { viewModel.watchFullScreenAdReviveCategory() },
                onForfeitRound = { viewModel.forfeitRound() },
                onExitQuiz = { viewModel.exitQuiz() },
                modifier = Modifier
                    .fillMaxSize()
                    .windowInsetsPadding(WindowInsets.navigationBars)
            )
        } else {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                containerColor = QuizDarkBg,
                topBar = {
                    QuizTopBar(
                        coinBalance = userStats.coins,
                        dayStreak = userStats.dayStreak,
                        currentLanguage = userStats.language,
                        onWalletClick = { viewModel.selectTab(NavigationTab.REWARD) },
                        onDailyStreakClick = { viewModel.claimDailyStreak() },
                        onLanguageClick = { viewModel.toggleLanguage() }
                    )
                },
                bottomBar = {
                    QuizBottomNavigationBar(
                        currentTab = currentTab,
                        onTabSelected = { viewModel.selectTab(it) },
                        modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars)
                    )
                },
                snackbarHost = { SnackbarHost(snackbarHostState) }
            ) { paddingValues ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    when (currentTab) {
                        NavigationTab.HOME -> HomeScreen(
                            userStats = userStats,
                            activityLog = activityLog,
                            isDailyClaimed = viewModel.repository.isDailyStreakClaimedToday(),
                            onPlayBullet = { viewModel.startBulletQuiz() },
                            onPlayCategory = { catId -> viewModel.startCategoryQuiz(catId) },
                            onClaimDaily = { viewModel.claimDailyStreak() },
                            onViewAllCategories = { viewModel.selectTab(NavigationTab.PLAY) },
                            onViewRewards = { viewModel.selectTab(NavigationTab.REWARD) }
                        )

                        NavigationTab.PLAY -> PlayScreen(
                            userStats = userStats,
                            onStartBullet = { viewModel.startBulletQuiz() },
                            onStartCategory = { catId -> viewModel.startCategoryQuiz(catId) }
                        )

                        NavigationTab.REWARD -> RewardScreen(
                            userStats = userStats,
                            availableRewards = viewModel.repository.availableRewards,
                            redeemRequests = redeemRequests,
                            selectedReward = selectedReward,
                            onSelectReward = { viewModel.setSelectedReward(it) },
                            onSubmitRedemption = { reward, detail ->
                                viewModel.requestRedeem(reward, detail)
                            },
                            isWatchingAd = isWatchingRewardAd,
                            adCountdown = rewardAdCountdown,
                            onWatchAdForCoins = { viewModel.watchRewardAdForCoins() }
                        )

                        NavigationTab.LEADERBOARD -> LeaderboardScreen(
                            userStats = userStats,
                            leaderboardUsers = viewModel.repository.getLeaderboard()
                        )

                        NavigationTab.PROFILE -> ProfileScreen(
                            userStats = userStats,
                            redeemRequests = redeemRequests,
                            showAdminSheet = showAdminSheet,
                            onToggleLanguage = { viewModel.toggleLanguage() },
                            onOpenAdminSheet = { viewModel.setShowAdminSheet(true) },
                            onCloseAdminSheet = { viewModel.setShowAdminSheet(false) },
                            onAdminDecideRedeem = { id, approve ->
                                viewModel.adminDecideRedeem(id, approve)
                            },
                            onGrantDemoCoins = { amount -> viewModel.grantDemoCoins(amount) },
                            onShowToast = { viewModel.showToast(it) }
                        )
                    }
                }
            }
        }
    }
}
