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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.model.RedeemRequest
import com.example.model.RedeemReward
import com.example.model.RedeemStatus
import com.example.model.UserStats
import com.example.ui.theme.AmberWarning
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
fun RewardScreen(
    userStats: UserStats,
    availableRewards: List<RedeemReward>,
    redeemRequests: List<RedeemRequest>,
    selectedReward: RedeemReward?,
    onSelectReward: (RedeemReward?) -> Unit,
    onSubmitRedemption: (RedeemReward, String) -> Unit,
    isWatchingAd: Boolean = false,
    adCountdown: Int = 5,
    onWatchAdForCoins: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val isBn = userStats.language == "bn"
    var accountInput by remember { mutableStateOf("") }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(QuizDarkBg)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 28.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Wallet Overview Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("wallet_overview_card"),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = QuizSurface),
                border = androidx.compose.foundation.BorderStroke(1.2.dp, GoldPrimary)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.linearGradient(
                                listOf(Color(0xFF291C03), QuizSurface)
                            )
                        )
                        .padding(18.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = if (isBn) "কয়েন ওয়ালেট" else "Coin Wallet",
                                fontSize = 14.sp,
                                color = GoldLight,
                                fontWeight = FontWeight.SemiBold
                            )
                            Icon(
                                imageVector = Icons.Default.AccountBalanceWallet,
                                contentDescription = null,
                                tint = GoldPrimary,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        Row(
                            verticalAlignment = Alignment.Bottom,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "${userStats.coins}",
                                fontSize = 36.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = GoldPrimary
                            )
                            Text(
                                text = if (isBn) "কয়েন" else "Coins",
                                fontSize = 16.sp,
                                color = GoldLight,
                                modifier = Modifier.padding(bottom = 6.dp)
                            )
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color(0x33000000))
                                .padding(horizontal = 14.dp, vertical = 10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = if (isBn) "সর্বমোট অর্জিত" else "Lifetime Earned",
                                    fontSize = 11.sp,
                                    color = TextLightMuted
                                )
                                Text(
                                    text = "${userStats.lifetimeCoins} coins",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextLightPrimary
                                )
                            }
                            Text(
                                text = "✨",
                                fontSize = 18.sp
                            )
                        }
                    }
                }
            }
        }

        // Watch AD and earn 20 Coin option (Placed after coin wallet dashboard)
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("watch_ad_earn_coins_card")
                    .clickable { onWatchAdForCoins() },
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = QuizSurfaceVariant),
                border = androidx.compose.foundation.BorderStroke(1.2.dp, EmeraldSuccess)
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
                                .background(EmeraldSuccess.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "🎬", fontSize = 24.sp)
                        }
                        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                            Text(
                                text = if (isBn) "বিজ্ঞাপন দেখুন এবং ২০ কয়েন পান" else "Watch AD and earn 20 Coins",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextLightPrimary
                            )
                            Text(
                                text = if (isBn) "একটি স্পন্সরড ভিডিও দেখুন ও সাথে সাথে ২০ কয়েন যোগ করুন" else "Watch a short sponsored video & earn instant 20 coins",
                                fontSize = 11.sp,
                                color = TextLightSecondary
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(EmeraldSuccess)
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = if (isBn) "+২০ কয়েন" else "+20 COINS",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )
                    }
                }
            }
        }

        // Section Title: Available Rewards
        item {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = if (isBn) "রিওয়ার্ড ভাউচার ক্যাটালগ" else "Voucher Redemptions",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextLightPrimary
                )
                Text(
                    text = if (isBn)
                        "পছন্দের রিওয়ার্ড নির্বাচন করুন এবং মোবাইল বা ইমেইল অ্যাকাউন্ট দিন"
                    else
                        "Select your reward and enter payout mobile wallet or email",
                    fontSize = 12.sp,
                    color = TextLightSecondary
                )
            }
        }

        // Available Reward Cards
        items(availableRewards) { reward ->
            val canAfford = userStats.coins >= reward.coinsRequired
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .clickable {
                        accountInput = ""
                        onSelectReward(reward)
                    }
                    .testTag("reward_item_${reward.id}"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = QuizSurface),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    if (canAfford) GoldPrimary.copy(alpha = 0.6f) else QuizBorder
                )
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
                                .size(44.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(
                                    if (reward.method.contains("bKash", true)) Color(0xFFD12053).copy(alpha = 0.2f)
                                    else if (reward.method.contains("Recharge", true)) Color(0xFF10B981).copy(alpha = 0.2f)
                                    else Color(0xFF0079C1).copy(alpha = 0.2f)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (reward.method.contains("Recharge", true))
                                    Icons.Default.PhoneAndroid else Icons.Default.MonetizationOn,
                                contentDescription = null,
                                tint = if (reward.method.contains("bKash", true)) Color(0xFFFF4D80)
                                else if (reward.method.contains("Recharge", true)) EmeraldSuccess
                                else CyanAccent,
                                modifier = Modifier.size(22.dp)
                            )
                        }

                        Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                            Text(
                                text = if (isBn) reward.titleBn else reward.titleEn,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextLightPrimary
                            )
                            Text(
                                text = reward.subtitle,
                                fontSize = 11.sp,
                                color = TextLightSecondary,
                                maxLines = 1
                            )
                            Text(
                                text = "${reward.coinsRequired} Coins required",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = if (canAfford) EmeraldSuccess else GoldPrimary
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (canAfford) GoldPrimary else Color(0xFF1E293B))
                            .padding(horizontal = 12.dp, vertical = 7.dp)
                    ) {
                        Text(
                            text = if (canAfford) (if (isBn) "রিডিম" else "REDEEM") else "${reward.coinsRequired}",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (canAfford) Color(0xFF1E1500) else TextLightMuted
                        )
                    }
                }
            }
        }

        // Section: My Redemptions History
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = if (isBn) "আমার রিডেম্পশন হিস্ট্রি" else "My Redemptions",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextLightPrimary
                )

                if (redeemRequests.isEmpty()) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = QuizSurface),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = if (isBn) "এখনও কোনো রিডেম্পশন আবেদন নেই" else "No redemption requests yet",
                            fontSize = 13.sp,
                            color = TextLightMuted,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                } else {
                    redeemRequests.forEach { req ->
                        RedeemHistoryRow(req = req, isBn = isBn)
                    }
                }
            }
        }
    }

    // REDEEM CONFIRMATION MODAL
    if (selectedReward != null) {
        val reward = selectedReward
        val canAfford = userStats.coins >= reward.coinsRequired

        Dialog(onDismissRequest = { onSelectReward(null) }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .testTag("redeem_dialog"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = QuizSurfaceVariant),
                border = androidx.compose.foundation.BorderStroke(1.2.dp, GoldPrimary)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Text(
                        text = if (isBn) "ভাউচার রিডিম করুন" else "Redeem Voucher",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = GoldLight
                    )

                    Text(
                        text = "${reward.amountText} (${reward.method})",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = TextLightPrimary
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xFF0F172A))
                            .padding(10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = if (isBn) "প্রয়োজনীয় কয়েন:" else "Required Coins:",
                            fontSize = 13.sp,
                            color = TextLightSecondary
                        )
                        Text(
                            text = "${reward.coinsRequired} Coins",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (canAfford) EmeraldSuccess else CrimsonStrike
                        )
                    }

                    if (!canAfford) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(Icons.Default.Warning, contentDescription = null, tint = AmberWarning, modifier = Modifier.size(16.dp))
                            Text(
                                text = if (isBn)
                                    "আপনার পর্যাপ্ত কয়েন নেই! আরো কুইজ খেলুন।"
                                else
                                    "Insufficient coins. Play more quizzes to earn coins!",
                                fontSize = 12.sp,
                                color = AmberWarning
                            )
                        }
                    } else {
                        OutlinedTextField(
                            value = accountInput,
                            onValueChange = { accountInput = it },
                            label = {
                                Text(
                                    if (reward.method.contains("PayPal", true))
                                        "PayPal Email Address"
                                    else
                                        "bKash / Nagad / Mobile Number"
                                )
                            },
                            placeholder = {
                                Text(
                                    if (reward.method.contains("PayPal", true))
                                        "e.g. user@gmail.com"
                                    else
                                        "e.g. 017xxxxxxxx"
                                )
                            },
                            keyboardOptions = if (reward.method.contains("PayPal", true))
                                KeyboardOptions(keyboardType = KeyboardType.Email)
                            else
                                KeyboardOptions(keyboardType = KeyboardType.Phone),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("account_input_field"),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = GoldPrimary,
                                unfocusedBorderColor = QuizBorder,
                                focusedTextColor = TextLightPrimary,
                                unfocusedTextColor = TextLightPrimary
                            ),
                            singleLine = true
                        )

                        Text(
                            text = if (isBn)
                                "পেমেন্ট ম্যানুয়াল অ্যাডমিন যাচাইয়ের পর ২৪ ঘণ্টার মধ্যে সম্পন্ন হয়।"
                            else
                                "Coins are held immediately and processed after administrative review.",
                            fontSize = 11.sp,
                            color = TextLightMuted
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedButton(
                            onClick = { onSelectReward(null) },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text(if (isBn) "বাতিল" else "Cancel")
                        }

                        Button(
                            onClick = {
                                if (canAfford && accountInput.isNotBlank()) {
                                    onSubmitRedemption(reward, accountInput)
                                }
                            },
                            enabled = canAfford && accountInput.isNotBlank(),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("confirm_redeem_button"),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = GoldPrimary,
                                contentColor = Color(0xFF1E1500)
                            )
                        ) {
                            Text(if (isBn) "নিশ্চিত করুন" else "Confirm", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }

    // WATCHING REWARD AD COUNTDOWN MODAL
    if (isWatchingAd) {
        Dialog(onDismissRequest = {}) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .testTag("reward_ad_modal"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = QuizDarkBg),
                border = androidx.compose.foundation.BorderStroke(1.dp, EmeraldSuccess)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    CircularProgressIndicator(
                        color = EmeraldSuccess,
                        modifier = Modifier.size(52.dp),
                        strokeWidth = 4.dp
                    )
                    Text(
                        text = if (isBn) "বিজ্ঞাপন চলছে..." else "Sponsored Ad Playing...",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextLightPrimary
                    )
                    Text(
                        text = "${adCountdown}s",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = GoldPrimary
                    )
                    Text(
                        text = if (isBn) "সম্পূর্ণ দেখুন ২০ কয়েন ওয়ালেটে পেতে" else "Watch until end to receive 20 coins",
                        fontSize = 12.sp,
                        color = TextLightMuted
                    )
                }
            }
        }
    }
}

@Composable
fun RedeemHistoryRow(req: RedeemRequest, isBn: Boolean) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = QuizSurface),
        border = androidx.compose.foundation.BorderStroke(1.dp, QuizBorder)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    text = req.rewardTitle,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextLightPrimary
                )
                Text(
                    text = "${req.payoutMethod} • ${req.accountDetail}",
                    fontSize = 11.sp,
                    color = TextLightSecondary
                )
                Text(
                    text = "${req.coinsCost} coins • ${req.dateFormatted}",
                    fontSize = 10.sp,
                    color = TextLightMuted
                )
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(
                        when (req.status) {
                            RedeemStatus.APPROVED -> EmeraldSuccess.copy(alpha = 0.2f)
                            RedeemStatus.PENDING -> AmberWarning.copy(alpha = 0.2f)
                            RedeemStatus.REJECTED -> CrimsonStrike.copy(alpha = 0.2f)
                        }
                    )
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = when (req.status) {
                        RedeemStatus.APPROVED -> if (isBn) "পরিশোধিত" else "PAID"
                        RedeemStatus.PENDING -> if (isBn) "অপেক্ষমাণ" else "PENDING"
                        RedeemStatus.REJECTED -> if (isBn) "বাতিল" else "REJECTED"
                    },
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = when (req.status) {
                        RedeemStatus.APPROVED -> EmeraldSuccess
                        RedeemStatus.PENDING -> AmberWarning
                        RedeemStatus.REJECTED -> CrimsonStrike
                    }
                )
            }
        }
    }
}
