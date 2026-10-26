package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
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
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.model.RedeemRequest
import com.example.model.RedeemStatus
import com.example.model.UserStats
import com.example.ui.theme.CrimsonStrike
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
fun ProfileScreen(
    userStats: UserStats,
    redeemRequests: List<RedeemRequest>,
    showAdminSheet: Boolean,
    onToggleLanguage: () -> Unit,
    onOpenAdminSheet: () -> Unit,
    onCloseAdminSheet: () -> Unit,
    onAdminDecideRedeem: (String, Boolean) -> Unit,
    onGrantDemoCoins: (Int) -> Unit,
    onShowToast: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val isBn = userStats.language == "bn"
    var soundEnabled by remember { mutableStateOf(true) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(QuizDarkBg)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 28.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // User Profile Header
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("profile_header_card"),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = QuizSurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, QuizBorder)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.linearGradient(listOf(GoldPrimary, IndigoAccent))
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = userStats.userName.take(1).uppercase(),
                            fontSize = 28.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )
                    }

                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = userStats.userName,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextLightPrimary
                            )
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(Color(0xFF2E2204))
                                    .border(1.dp, GoldPrimary, RoundedCornerShape(6.dp))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "PRO",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = GoldPrimary
                                )
                            }
                        }

                        Text(
                            text = if (isBn) "সদস্য স্ট্যাটাস: সক্রিয়" else "Player ID: #QE-9821 • Active",
                            fontSize = 12.sp,
                            color = TextLightMuted
                        )

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(Icons.Default.MonetizationOn, contentDescription = null, tint = GoldPrimary, modifier = Modifier.size(16.dp))
                            Text(
                                text = "${userStats.coins} Coins",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = GoldLight
                            )
                        }
                    }
                }
            }
        }

        // Stats Matrix
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = QuizSurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, QuizBorder)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = if (isBn) "খেলার পরিসংখ্যান" else "Quiz Performance",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextLightPrimary
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        ProfileStatItem(
                            label = if (isBn) "মোট সঠিক" else "Total Correct",
                            value = "${userStats.totalCorrect}",
                            color = EmeraldSuccess
                        )
                        val acc = if (userStats.totalAnswered > 0)
                            (userStats.totalCorrect * 100) / userStats.totalAnswered else 0
                        ProfileStatItem(
                            label = if (isBn) "গড় নির্ভুলতা" else "Accuracy",
                            value = "$acc%",
                            color = CyanAccent
                        )
                        ProfileStatItem(
                            label = if (isBn) "সর্বোচ্চ স্ট্রিক" else "Best Streak",
                            value = "${userStats.highestStreak}x",
                            color = GoldPrimary
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    // Lifeline Inventory Row (Skip lifeline)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xFF0F172A))
                            .padding(10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (isBn) "লাইফলাইন ইনভেন্টরি:" else "Lifelines Owned:",
                            fontSize = 12.sp,
                            color = TextLightMuted
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(text = "⚡", fontSize = 14.sp)
                            Text(
                                text = "Skip: ${userStats.skipCount}",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = CyanAccent
                            )
                        }
                    }
                }
            }
        }

        // Referral Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("referral_card"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = QuizSurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, GoldPrimary.copy(alpha = 0.5f))
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(Icons.Default.CardGiftcard, contentDescription = null, tint = GoldPrimary)
                        Text(
                            text = if (isBn) "বন্ধুদের আমন্ত্রণ জানান" else "Invite & Earn +100 Coins",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextLightPrimary
                        )
                    }

                    Text(
                        text = if (isBn)
                            "আপনার রেফারেল কোড দিয়ে বন্ধুদের যুক্ত করুন এবং দুজনেই পাবেন ফ্রি কয়েন!"
                        else
                            "Share your unique referral code with friends and earn +100 bonus coins on sign-up!",
                        fontSize = 12.sp,
                        color = TextLightSecondary
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xFF0F172A))
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = userStats.referralCode,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = GoldLight,
                            letterSpacing = 2.sp
                        )

                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            IconButton(
                                onClick = {
                                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                    clipboard.setPrimaryClip(ClipData.newPlainText("Referral", userStats.referralCode))
                                    onShowToast("Referral code copied!")
                                },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(Icons.Default.ContentCopy, contentDescription = "Copy", tint = CyanAccent, modifier = Modifier.size(18.dp))
                            }

                            IconButton(
                                onClick = {
                                    val sendIntent = Intent().apply {
                                        action = Intent.ACTION_SEND
                                        putExtra(Intent.EXTRA_TEXT, "Join QuizEarn with my code ${userStats.referralCode} and earn free coins!")
                                        type = "text/plain"
                                    }
                                    context.startActivity(Intent.createChooser(sendIntent, "Share Referral Code"))
                                },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(Icons.Default.Share, contentDescription = "Share", tint = GoldPrimary, modifier = Modifier.size(18.dp))
                            }
                        }
                    }
                }
            }
        }

        // Settings / Preferences
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = QuizSurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, QuizBorder)
            ) {
                Column(
                    modifier = Modifier.padding(vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    // Language Switch Row
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onToggleLanguage() }
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Icon(Icons.Default.Translate, contentDescription = null, tint = CyanAccent)
                            Column {
                                Text(
                                    text = if (isBn) "ভাষা (Language)" else "App Language",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = TextLightPrimary
                                )
                                Text(
                                    text = if (isBn) "বর্তমান: বাংলা" else "Current: English",
                                    fontSize = 11.sp,
                                    color = TextLightMuted
                                )
                            }
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFF1E293B))
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = if (isBn) "বাংলা" else "English",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = CyanAccent
                            )
                        }
                    }

                    // Sound Toggle Row
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Icon(Icons.AutoMirrored.Filled.VolumeUp, contentDescription = null, tint = GoldPrimary)
                            Text(
                                text = if (isBn) "সাউন্ড ও হ্যাপটিক্স" else "Sound & Haptics",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = TextLightPrimary
                            )
                        }
                        Switch(
                            checked = soundEnabled,
                            onCheckedChange = {
                                soundEnabled = it
                                onShowToast(if (it) "Audio enabled" else "Audio muted")
                            },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color(0xFF1E1500),
                                checkedTrackColor = GoldPrimary
                            )
                        )
                    }
                }
            }
        }

        // Admin Sheet Access Button
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onOpenAdminSheet() }
                    .testTag("open_admin_sheet_button"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = QuizSurfaceVariant),
                border = androidx.compose.foundation.BorderStroke(1.dp, IndigoAccent.copy(alpha = 0.5f))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AdminPanelSettings,
                            contentDescription = null,
                            tint = IndigoAccent,
                            modifier = Modifier.size(24.dp)
                        )
                        Column {
                            Text(
                                text = if (isBn) "অ্যাডমিন ডেটা শিট" else "Admin Data Sheet",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextLightPrimary
                            )
                            Text(
                                text = if (isBn) "রিডেম্পশন যাচাই ও টেস্ট কয়েন গ্র্যান্ট" else "Manage payouts & test demo tools",
                                fontSize = 11.sp,
                                color = TextLightMuted
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(IndigoAccent.copy(alpha = 0.2f))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "ADMIN",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = IndigoAccent
                        )
                    }
                }
            }
        }
    }

    // ADMIN SHEET MODAL
    if (showAdminSheet) {
        Dialog(onDismissRequest = onCloseAdminSheet) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(550.dp)
                    .padding(4.dp)
                    .testTag("admin_sheet_modal"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = QuizDarkBg),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, IndigoAccent)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Header
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(Icons.Default.AdminPanelSettings, contentDescription = null, tint = IndigoAccent)
                            Text(
                                text = "Admin Control Sheet",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextLightPrimary
                            )
                        }
                        IconButton(onClick = onCloseAdminSheet) {
                            Icon(Icons.Default.Close, contentDescription = "Close", tint = TextLightMuted)
                        }
                    }

                    // Demo Tool: Grant Coins
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = QuizSurface),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "⚡ Demo Tools (Instant Testing)",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = GoldPrimary
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Button(
                                    onClick = { onGrantDemoCoins(1000) },
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(8.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E293B))
                                ) {
                                    Text("+1,000 Coins", fontSize = 11.sp, color = GoldLight)
                                }
                                Button(
                                    onClick = { onGrantDemoCoins(5000) },
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(8.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary)
                                ) {
                                    Text("+5,000 Coins", fontSize = 11.sp, color = Color(0xFF1E1500), fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }

                    // Pending Payouts Queue
                    Text(
                        text = "Pending Redemptions (${redeemRequests.count { it.status == RedeemStatus.PENDING }})",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextLightPrimary
                    )

                    val pendingList = redeemRequests.filter { it.status == RedeemStatus.PENDING }
                    if (pendingList.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .background(QuizSurface),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "No pending payouts in queue.\nSubmit a redemption on the Reward tab to test!",
                                fontSize = 12.sp,
                                color = TextLightMuted,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(pendingList) { req ->
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = CardDefaults.cardColors(containerColor = QuizSurface),
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Column(
                                        modifier = Modifier.padding(10.dp),
                                        verticalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Text(
                                            text = "${req.rewardTitle} • ${req.coinsCost} coins",
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = TextLightPrimary
                                        )
                                        Text(
                                            text = "Account: ${req.accountDetail} (${req.payoutMethod})",
                                            fontSize = 11.sp,
                                            color = TextLightSecondary
                                        )
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            Button(
                                                onClick = { onAdminDecideRedeem(req.id, true) },
                                                modifier = Modifier.weight(1f),
                                                shape = RoundedCornerShape(6.dp),
                                                colors = ButtonDefaults.buttonColors(containerColor = EmeraldSuccess)
                                            ) {
                                                Text("Approve & Paid", fontSize = 10.sp, color = Color.White)
                                            }
                                            Button(
                                                onClick = { onAdminDecideRedeem(req.id, false) },
                                                modifier = Modifier.weight(1f),
                                                shape = RoundedCornerShape(6.dp),
                                                colors = ButtonDefaults.buttonColors(containerColor = CrimsonStrike)
                                            ) {
                                                Text("Reject & Refund", fontSize = 10.sp, color = Color.White)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProfileStatItem(label: String, value: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            fontSize = 18.sp,
            fontWeight = FontWeight.ExtraBold,
            color = color
        )
        Text(
            text = label,
            fontSize = 11.sp,
            color = TextLightMuted
        )
    }
}
