package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.LeaderboardUser
import com.example.model.UserStats
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.GoldDark
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.GoldSecondary
import com.example.ui.theme.QuizBorder
import com.example.ui.theme.QuizDarkBg
import com.example.ui.theme.QuizSurface
import com.example.ui.theme.QuizSurfaceVariant
import com.example.ui.theme.TextLightMuted
import com.example.ui.theme.TextLightPrimary
import com.example.ui.theme.TextLightSecondary

@Composable
fun LeaderboardScreen(
    userStats: UserStats,
    leaderboardUsers: List<LeaderboardUser>,
    modifier: Modifier = Modifier
) {
    val isBn = userStats.language == "bn"
    val top1 = leaderboardUsers.find { it.rank == 1 }
    val top2 = leaderboardUsers.find { it.rank == 2 }
    val top3 = leaderboardUsers.find { it.rank == 3 }
    val others = leaderboardUsers.filter { it.rank > 3 }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(QuizDarkBg)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 28.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
        item {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = if (isBn) "🏆 শীর্ষ লিডারবোর্ড" else "🏆 Global Leaderboard",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextLightPrimary
                )
                Text(
                    text = if (isBn) "সাপ্তাহিক শীর্ষ খেলোয়াড় ও র‍্যাঙ্কিং" else "Weekly Top QuizMasters & Coin Earners",
                    fontSize = 12.sp,
                    color = TextLightSecondary
                )
            }
        }

        // Top 3 Podium
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("podium_card"),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = QuizSurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, QuizBorder)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 18.dp, horizontal = 12.dp),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.Bottom
                ) {
                    // Rank 2 (Silver)
                    if (top2 != null) {
                        PodiumColumn(
                            user = top2,
                            badgeColor = Color(0xFF94A3B8),
                            crown = "🥈",
                            height = 110.dp
                        )
                    }

                    // Rank 1 (Gold - Center)
                    if (top1 != null) {
                        PodiumColumn(
                            user = top1,
                            badgeColor = GoldPrimary,
                            crown = "👑",
                            height = 135.dp
                        )
                    }

                    // Rank 3 (Bronze)
                    if (top3 != null) {
                        PodiumColumn(
                            user = top3,
                            badgeColor = Color(0xFFD97706),
                            crown = "🥉",
                            height = 95.dp
                        )
                    }
                }
            }
        }

        // Section Title: Rankings List
        item {
            Text(
                text = if (isBn) "পরবর্তী র‍্যাঙ্কিং" else "Rankings",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = TextLightPrimary
            )
        }

        // Leaderboard List
        items(others) { user ->
            LeaderboardRow(user = user, isBn = isBn)
        }
    }
}

@Composable
fun PodiumColumn(
    user: LeaderboardUser,
    badgeColor: Color,
    crown: String,
    height: androidx.compose.ui.unit.Dp
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(text = crown, fontSize = 20.sp)

        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(Color(user.avatarBgHex))
                .border(2.dp, badgeColor, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = user.name.take(1),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        Text(
            text = user.name.split(" ").firstOrNull() ?: user.name,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = TextLightPrimary,
            maxLines = 1
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Icon(Icons.Default.MonetizationOn, contentDescription = null, tint = GoldPrimary, modifier = Modifier.size(12.dp))
            Text(
                text = "${user.coins}",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = GoldLight
            )
        }

        // Podium Block
        Box(
            modifier = Modifier
                .width(80.dp)
                .height(height)
                .clip(RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp))
                .background(
                    Brush.verticalGradient(
                        listOf(badgeColor.copy(alpha = 0.35f), Color(0xFF0F172A))
                    )
                )
                .border(
                    1.dp,
                    badgeColor.copy(alpha = 0.5f),
                    RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "#${user.rank}",
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                color = badgeColor
            )
        }
    }
}

@Composable
fun LeaderboardRow(user: LeaderboardUser, isBn: Boolean) {
    val isCurrent = user.isCurrentUser
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("leaderboard_row_${user.rank}"),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isCurrent) Color(0xFF261D03) else QuizSurface
        ),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (isCurrent) GoldPrimary else QuizBorder
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "#${user.rank}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isCurrent) GoldPrimary else TextLightMuted,
                    modifier = Modifier.width(30.dp)
                )

                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color(user.avatarBgHex)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = user.name.take(1),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = user.name,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextLightPrimary
                        )
                        if (isCurrent) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(GoldPrimary)
                                    .padding(horizontal = 6.dp, vertical = 1.dp)
                            ) {
                                Text(
                                    text = if (isBn) "আপনি" else "YOU",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF1E1500)
                                )
                            }
                        }
                    }
                    Text(
                        text = "${user.accuracy}% accuracy",
                        fontSize = 11.sp,
                        color = TextLightMuted
                    )
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(Icons.Default.MonetizationOn, contentDescription = null, tint = GoldPrimary, modifier = Modifier.size(16.dp))
                Text(
                    text = "${user.coins}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = GoldLight
                )
            }
        }
    }
}
