package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Fireplace
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.QuestionBank
import com.example.model.ActivityItem
import com.example.model.QuizCategoryInfo
import com.example.model.UserStats
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.GoldDark
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
fun HomeScreen(
    userStats: UserStats,
    activityLog: List<ActivityItem>,
    isDailyClaimed: Boolean,
    onPlayBullet: () -> Unit,
    onPlayCategory: (String) -> Unit,
    onClaimDaily: () -> Unit,
    onViewAllCategories: () -> Unit,
    onViewRewards: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isBn = userStats.language == "bn"

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(QuizDarkBg)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 8.dp, bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Hero Card - Bullet Quiz
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("bullet_quiz_hero_card"),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = QuizSurface),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    Image(
                        painter = painterResource(id = R.drawable.banner_bullet_quiz),
                        contentDescription = "Bullet Quiz Banner",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp),
                        contentScale = ContentScale.Crop
                    )
                    // Gradient overlay
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .background(
                                Brush.verticalGradient(
                                    listOf(
                                        Color.Transparent,
                                        QuizDarkBg.copy(alpha = 0.6f),
                                        QuizSurface
                                    )
                                )
                            )
                    )
                    // Lightning Badge
                    Row(
                        modifier = Modifier
                            .padding(12.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xE60B1020))
                            .border(1.dp, GoldPrimary, RoundedCornerShape(12.dp))
                            .padding(horizontal = 10.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Bolt,
                            contentDescription = null,
                            tint = GoldPrimary,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = if (isBn) "গতিময় বুলেট কুইজ" else "POPULAR • BULLET",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = GoldLight
                        )
                    }
                }

                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = if (isBn) "বুলেট কুইজ খেলুন" else "Bullet Quiz Challenge",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextLightPrimary
                    )
                    Text(
                        text = if (isBn)
                            "২০টি প্রশ্ন • প্রশ্ন প্রতি ৫ সেকেন্ড • ২০ কয়েন • ৩ স্ট্রাইকে আউট!"
                        else
                            "20 fast questions • 5s countdown • 20 coins/Q • 3 strikes rule!",
                        fontSize = 13.sp,
                        color = TextLightSecondary,
                        lineHeight = 18.sp
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Button(
                        onClick = onPlayBullet,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("start_bullet_quiz_button"),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = GoldPrimary,
                            contentColor = Color(0xFF1E1500)
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (isBn) "বুলেট কুইজ শুরু করুন" else "PLAY BULLET QUIZ",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }

        // Daily Check-In Streak Banner
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("daily_streak_card")
                    .clickable { if (!isDailyClaimed) onClaimDaily() },
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = QuizSurfaceVariant),
                border = androidx.compose.foundation.BorderStroke(1.dp, QuizBorder)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF312204)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "🔥", fontSize = 22.sp)
                        }
                        Column {
                            Text(
                                text = if (isBn) "দৈনিক বোনাস • দিন ${userStats.dayStreak}" else "Daily Streak • Day ${userStats.dayStreak}",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextLightPrimary
                            )
                            Text(
                                text = if (isDailyClaimed)
                                    (if (isBn) "আজকে কয়েন গ্রহণ করা হয়েছে!" else "Claimed today! Come back tomorrow")
                                else
                                    (if (isBn) "+২৫ কয়েন গ্রহণ করতে ট্যাপ করুন" else "Tap to claim +25 Free Coins!"),
                                fontSize = 12.sp,
                                color = if (isDailyClaimed) EmeraldSuccess else GoldPrimary
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (isDailyClaimed) Color(0xFF1E293B) else GoldPrimary)
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = if (isDailyClaimed) (if (isBn) "গৃহীত" else "CLAIMED") else (if (isBn) "+২৫ কয়েন" else "+25 COINS"),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isDailyClaimed) TextLightMuted else Color(0xFF1E1500)
                        )
                    }
                }
            }
        }

        // AdMob Banner Ad Slot (Clear space for AdMob Banner AD)
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(68.dp)
                    .testTag("admob_banner_ad_slot"),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = QuizSurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, QuizBorder)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 14.dp, vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(Color(0xFF1E293B))
                                    .border(1.dp, QuizBorder, RoundedCornerShape(6.dp))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "Ad",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = GoldPrimary
                                )
                            }
                            Column {
                                Text(
                                    text = "Google AdMob • Banner Ad Slot",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextLightPrimary
                                )
                                Text(
                                    text = "Adaptive 320x50 Banner Slot • Active",
                                    fontSize = 10.sp,
                                    color = TextLightMuted
                                )
                            }
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFF0F172A))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "320 x 50",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = CyanAccent
                            )
                        }
                    }
                }
            }
        }

        // Categories Section Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (isBn) "ক্যাটাগরি সমূহ" else "Explore Categories",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextLightPrimary
                )
                Text(
                    text = if (isBn) "সব দেখুন" else "View All",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = CyanAccent,
                    modifier = Modifier
                        .clickable { onViewAllCategories() }
                        .padding(4.dp)
                )
            }
        }

        // Categories Carousel
        item {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(QuestionBank.categories) { cat ->
                    CategoryMiniCard(
                        category = cat,
                        isBn = isBn,
                        onClick = { onPlayCategory(cat.id) }
                    )
                }
            }
        }

        // Recent Activity Feed
        item {
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = if (isBn) "সাম্প্রতিক কার্যক্রম" else "Recent Activity",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextLightPrimary
                )

                if (activityLog.isEmpty()) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = QuizSurface),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = if (isBn) "কোনো কার্যক্রম নেই। কুইজ খেলা শুরু করুন!" else "No activity yet. Start playing a quiz!",
                            fontSize = 13.sp,
                            color = TextLightMuted,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                } else {
                    activityLog.take(4).forEach { item ->
                        ActivityRowItem(item = item)
                    }
                }
            }
        }
    }
}

@Composable
fun StatCard(
    title: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    tint: Color,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null
) {
    Card(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .clickable(enabled = onClick != null) { onClick?.invoke() },
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = QuizSurface),
        border = androidx.compose.foundation.BorderStroke(1.dp, QuizBorder)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = tint,
                modifier = Modifier.size(20.dp)
            )
            Text(
                text = value,
                fontSize = 18.sp,
                fontWeight = FontWeight.ExtraBold,
                color = TextLightPrimary
            )
            Text(
                text = title,
                fontSize = 11.sp,
                color = TextLightMuted
            )
        }
    }
}

@Composable
fun CategoryMiniCard(
    category: QuizCategoryInfo,
    isBn: Boolean,
    onClick: () -> Unit
) {
    val cardColor = Color(category.colorHex)
    Card(
        modifier = Modifier
            .width(150.dp)
            .height(130.dp)
            .clip(RoundedCornerShape(14.dp))
            .clickable { onClick() }
            .testTag("cat_card_${category.id}"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = QuizSurface),
        border = androidx.compose.foundation.BorderStroke(1.dp, QuizBorder)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(cardColor.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = category.emoji,
                    fontSize = 22.sp
                )
            }
            Column {
                Text(
                    text = if (isBn) category.titleBn else category.titleEn,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextLightPrimary,
                    maxLines = 1
                )
                Text(
                    text = if (isBn) category.descriptionBn else category.descriptionEn,
                    fontSize = 10.sp,
                    color = TextLightMuted,
                    maxLines = 1
                )
            }
        }
    }
}

@Composable
fun ActivityRowItem(item: ActivityItem) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = QuizSurface),
        border = androidx.compose.foundation.BorderStroke(1.dp, QuizBorder)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.title,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextLightPrimary
                )
                Text(
                    text = "${item.description} • ${item.timestampFormatted}",
                    fontSize = 11.sp,
                    color = TextLightMuted
                )
            }

            if (item.coinDelta != 0) {
                Text(
                    text = if (item.coinDelta > 0) "+${item.coinDelta}" else "${item.coinDelta}",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (item.coinDelta > 0) EmeraldSuccess else GoldPrimary
                )
            }
        }
    }
}
