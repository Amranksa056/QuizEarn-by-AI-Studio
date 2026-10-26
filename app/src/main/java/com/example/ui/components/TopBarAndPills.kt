package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material.icons.outlined.EmojiEvents
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.MonetizationOn
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.NavigationTab
import com.example.ui.theme.CrimsonStrike
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.GoldSecondary
import com.example.ui.theme.QuizBorder
import com.example.ui.theme.QuizDarkBg
import com.example.ui.theme.QuizSurface
import com.example.ui.theme.TextLightMuted
import com.example.ui.theme.TextLightPrimary

@Composable
fun QuizTopBar(
    coinBalance: Int,
    dayStreak: Int,
    currentLanguage: String,
    onWalletClick: () -> Unit,
    onDailyStreakClick: () -> Unit,
    onLanguageClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(QuizDarkBg)
            .padding(horizontal = 16.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Brand Mark
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.testTag("brand_header")
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(
                        Brush.linearGradient(listOf(GoldPrimary, GoldSecondary))
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Q",
                    color = Color(0xFF1E1500),
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 20.sp
                )
            }
            Text(
                text = "QuizEarn",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp,
                    color = TextLightPrimary
                )
            )
        }

        // Action Pills
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Language Pill
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(QuizSurface)
                    .border(1.dp, QuizBorder, RoundedCornerShape(20.dp))
                    .clickable { onLanguageClick() }
                    .padding(horizontal = 10.dp, vertical = 6.dp)
                    .testTag("lang_toggle_button"),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Translate,
                    contentDescription = "Language",
                    tint = CyanAccent,
                    modifier = Modifier.size(14.dp)
                )
                Text(
                    text = if (currentLanguage == "bn") "বাংলা" else "EN",
                    color = TextLightPrimary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            // Streak Pill
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(QuizSurface)
                    .border(1.dp, QuizBorder, RoundedCornerShape(20.dp))
                    .clickable { onDailyStreakClick() }
                    .padding(horizontal = 10.dp, vertical = 6.dp)
                    .testTag("streak_pill"),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(text = "🔥", fontSize = 12.sp)
                Text(
                    text = "${dayStreak}d",
                    color = TextLightPrimary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // Wallet Coins Pill
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        Brush.horizontalGradient(
                            listOf(Color(0xFF2C2208), Color(0xFF1F1805))
                        )
                    )
                    .border(1.dp, GoldPrimary, RoundedCornerShape(20.dp))
                    .clickable { onWalletClick() }
                    .padding(horizontal = 12.dp, vertical = 6.dp)
                    .testTag("wallet_balance_pill"),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.MonetizationOn,
                    contentDescription = "Coins",
                    tint = GoldPrimary,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = coinBalance.toString(),
                    color = GoldLight,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun QuizBottomNavigationBar(
    currentTab: NavigationTab,
    onTabSelected: (NavigationTab) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier.fillMaxWidth(),
        containerColor = QuizSurface,
        tonalElevation = 8.dp
    ) {
        val items = listOf(
            Triple(NavigationTab.HOME, "Home", Icons.Filled.Home to Icons.Outlined.Home),
            Triple(NavigationTab.PLAY, "Play", Icons.Filled.PlayArrow to Icons.Outlined.PlayArrow),
            Triple(NavigationTab.REWARD, "Reward", Icons.Filled.MonetizationOn to Icons.Outlined.MonetizationOn),
            Triple(NavigationTab.LEADERBOARD, "Rank", Icons.Filled.EmojiEvents to Icons.Outlined.EmojiEvents),
            Triple(NavigationTab.PROFILE, "Profile", Icons.Filled.Person to Icons.Outlined.Person)
        )

        items.forEach { (tab, label, icons) ->
            val isSelected = currentTab == tab
            NavigationBarItem(
                selected = isSelected,
                onClick = { onTabSelected(tab) },
                icon = {
                    Icon(
                        imageVector = if (isSelected) icons.first else icons.second,
                        contentDescription = label,
                        modifier = Modifier.size(24.dp)
                    )
                },
                label = {
                    Text(
                        text = label,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color(0xFF1E1500),
                    selectedTextColor = GoldPrimary,
                    indicatorColor = GoldPrimary,
                    unselectedIconColor = TextLightMuted,
                    unselectedTextColor = TextLightMuted
                ),
                modifier = Modifier.testTag("nav_tab_${tab.name.lowercase()}")
            )
        }
    }
}

@Composable
fun CountdownTimerBar(
    timeRemainingSeconds: Int,
    maxTimeSeconds: Int,
    modifier: Modifier = Modifier
) {
    val progress = (timeRemainingSeconds.toFloat() / maxTimeSeconds.toFloat()).coerceIn(0f, 1f)
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(300),
        label = "timerProgress"
    )

    val barColor by animateColorAsState(
        targetValue = when {
            progress > 0.5f -> EmeraldSuccess
            progress > 0.25f -> GoldPrimary
            else -> CrimsonStrike
        },
        animationSpec = tween(300),
        label = "timerColor"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(8.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(Color(0xFF1E293B))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(animatedProgress)
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(barColor)
        )
    }
}

@Composable
fun StrikesIndicator(
    strikes: Int,
    maxStrikes: Int = 3,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "STRIKES:",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = CrimsonStrike
        )
        for (i in 1..maxStrikes) {
            val isStriked = i <= strikes
            Box(
                modifier = Modifier
                    .size(22.dp)
                    .clip(CircleShape)
                    .background(if (isStriked) CrimsonStrike else Color(0xFF1E293B))
                    .border(
                        1.dp,
                        if (isStriked) CrimsonStrike else QuizBorder,
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (isStriked) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Strike $i",
                        tint = Color.White,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
        }
    }
}
