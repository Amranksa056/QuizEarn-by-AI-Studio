package com.example.ui.screens

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import com.example.data.QuestionBank
import com.example.model.QuizCategoryInfo
import com.example.model.UserStats
import com.example.ui.theme.CrimsonStrike
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.EmeraldSuccess
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
fun PlayScreen(
    userStats: UserStats,
    onStartBullet: () -> Unit,
    onStartCategory: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val isBn = userStats.language == "bn"

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(QuizDarkBg)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 28.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Bullet Quiz Featured Box
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("play_bullet_card"),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = QuizSurface),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, GoldPrimary)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(
                                        Brush.linearGradient(
                                            listOf(GoldPrimary, GoldSecondary)
                                        )
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Bolt,
                                    contentDescription = null,
                                    tint = Color(0xFF1E1500),
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                            Column {
                                Text(
                                    text = if (isBn) "বুলেট কুইজ" else "Bullet Quiz",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextLightPrimary
                                )
                                Text(
                                    text = if (isBn) "গতি ও নির্ভুলতার চ্যালেঞ্জ" else "Speed & Accuracy Mode",
                                    fontSize = 12.sp,
                                    color = GoldLight
                                )
                            }
                        }

                        // Badge
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFF2E1C03))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = if (isBn) "২০ কয়েন/প্রশ্ন" else "20 Coins / Q",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = GoldPrimary
                            )
                        }
                    }

                    // Bullet Rules Row
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFF0F172A))
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        BulletRuleItem("⏱️", if (isBn) "৫ সেকেন্ড" else "5s Clock", if (isBn) "প্রশ্ন প্রতি" else "per question")
                        BulletRuleItem("🎯", if (isBn) "২০টি প্রশ্ন" else "20 Questions", if (isBn) "সব বিষয়" else "all topics")
                        BulletRuleItem("❌", if (isBn) "৩ স্ট্রাইক" else "3 Strikes", if (isBn) "সর্বোচ্চ ভুল" else "max wrong")
                    }

                    Button(
                        onClick = onStartBullet,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(46.dp)
                            .testTag("play_bullet_action_button"),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = GoldPrimary,
                            contentColor = Color(0xFF1E1500)
                        )
                    ) {
                        Icon(imageVector = Icons.Default.PlayArrow, contentDescription = null)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (isBn) "বুলেট কুইজ খেলুন" else "START BULLET QUIZ",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }

        // Section Title
        item {
            Text(
                text = if (isBn) "ক্যাটাগরি রাউন্ড" else "Category Rounds",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = TextLightPrimary
            )
        }

        // Category Cards List
        items(QuestionBank.categories) { cat ->
            CategoryRoundCard(
                category = cat,
                isBn = isBn,
                onStart = { onStartCategory(cat.id) }
            )
        }
    }
}

@Composable
fun BulletRuleItem(emoji: String, title: String, subtitle: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = emoji, fontSize = 16.sp)
        Text(
            text = title,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = TextLightPrimary
        )
        Text(
            text = subtitle,
            fontSize = 10.sp,
            color = TextLightMuted
        )
    }
}

@Composable
fun CategoryRoundCard(
    category: QuizCategoryInfo,
    isBn: Boolean,
    onStart: () -> Unit
) {
    val catColor = Color(category.colorHex)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable { onStart() }
            .testTag("category_play_${category.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = QuizSurface),
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
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(catColor.copy(alpha = 0.2f))
                        .border(1.dp, catColor.copy(alpha = 0.4f), RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = category.emoji,
                        fontSize = 24.sp
                    )
                }

                Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                    Text(
                        text = if (isBn) category.titleBn else category.titleEn,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextLightPrimary
                    )
                    Text(
                        text = if (isBn) category.descriptionBn else category.descriptionEn,
                        fontSize = 12.sp,
                        color = TextLightSecondary,
                        maxLines = 1
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "20 Qs",
                            fontSize = 11.sp,
                            color = catColor,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(text = "•", fontSize = 11.sp, color = TextLightMuted)
                        Text(
                            text = if (isBn) "৮ সেকেন্ড" else "8s clock",
                            fontSize = 11.sp,
                            color = TextLightMuted
                        )
                        Text(text = "•", fontSize = 11.sp, color = TextLightMuted)
                        Text(
                            text = if (isBn) "স্কিপ লাইফলাইন" else "Skip Lifeline",
                            fontSize = 11.sp,
                            color = EmeraldSuccess
                        )
                    }
                }
            }

            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "Start",
                tint = TextLightMuted,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}
