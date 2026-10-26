package com.example.data

import android.content.Context
import android.content.SharedPreferences
import com.example.model.ActivityItem
import com.example.model.LeaderboardUser
import com.example.model.LifelineType
import com.example.model.RedeemRequest
import com.example.model.RedeemReward
import com.example.model.RedeemStatus
import com.example.model.UserStats
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class QuizRepository(private val context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("quizearn_prefs", Context.MODE_PRIVATE)

    private val _userStats = MutableStateFlow(loadUserStats())
    val userStats: StateFlow<UserStats> = _userStats.asStateFlow()

    private val _redeemRequests = MutableStateFlow(loadRedeemRequests())
    val redeemRequests: StateFlow<List<RedeemRequest>> = _redeemRequests.asStateFlow()

    private val _activityLog = MutableStateFlow(loadActivityLog())
    val activityLog: StateFlow<List<ActivityItem>> = _activityLog.asStateFlow()

    val availableRewards = listOf(
        RedeemReward(
            id = "bkash_1000",
            titleEn = "৳1,000 bKash / Nagad",
            titleBn = "৳১,০০০ বিকাশ / নগদ",
            coinsRequired = 5000,
            amountText = "৳1,000",
            currency = "BDT",
            method = "bKash / Nagad",
            subtitle = "Direct instant mobile wallet transfer"
        ),
        RedeemReward(
            id = "recharge_150",
            titleEn = "৳150 Mobile Recharge",
            titleBn = "৳১৫০ মোবাইল রিচার্জ",
            coinsRequired = 1000,
            amountText = "৳150",
            currency = "BDT",
            method = "Mobile Recharge",
            subtitle = "Grameenphone, Banglalink, Robi, Airtel, Teletalk"
        ),
        RedeemReward(
            id = "recharge_250",
            titleEn = "৳250 Mobile Recharge",
            titleBn = "৳২৫০ মোবাইল রিচার্জ",
            coinsRequired = 2000,
            amountText = "৳250",
            currency = "BDT",
            method = "Mobile Recharge",
            subtitle = "Prepaid or Postpaid instant top-up"
        ),
        RedeemReward(
            id = "bkash_500",
            titleEn = "৳500 bKash / Nagad",
            titleBn = "৳৫০০ বিকাশ / নগদ",
            coinsRequired = 3000,
            amountText = "৳500",
            currency = "BDT",
            method = "bKash / Nagad",
            subtitle = "Personal or Agent mobile account"
        ),
        RedeemReward(
            id = "paypal_10",
            titleEn = "$10 PayPal Global Voucher",
            titleBn = "$১০ পেপ্যাল গ্লোবাল ভাউচার",
            coinsRequired = 8000,
            amountText = "$10.00",
            currency = "USD",
            method = "PayPal",
            subtitle = "International USD digital payment"
        )
    )

    private fun loadUserStats(): UserStats {
        val coins = prefs.getInt("coins", 250)
        val lifetimeCoins = prefs.getInt("lifetime_coins", 250)
        val dayStreak = prefs.getInt("day_streak", 1)
        val lastStreakDate = prefs.getString("last_streak_date", "") ?: ""
        val roundsPlayed = prefs.getInt("rounds_played", 0)
        val totalCorrect = prefs.getInt("total_correct", 0)
        val totalAnswered = prefs.getInt("total_answered", 0)
        val highestStreak = prefs.getInt("highest_streak", 0)
        val skip = prefs.getInt("skip", 3)
        val referral = prefs.getString("referral_code", "QE" + (1000..9999).random()) ?: "QE7721"
        val userName = prefs.getString("user_name", "QuizHero") ?: "QuizHero"
        val language = prefs.getString("language", "en") ?: "en"

        return UserStats(
            coins = coins,
            lifetimeCoins = lifetimeCoins,
            dayStreak = dayStreak,
            lastStreakDate = lastStreakDate,
            roundsPlayed = roundsPlayed,
            totalCorrect = totalCorrect,
            totalAnswered = totalAnswered,
            highestStreak = highestStreak,
            skipCount = skip,
            referralCode = referral,
            userName = userName,
            language = language
        )
    }

    private fun saveUserStats(stats: UserStats) {
        prefs.edit().apply {
            putInt("coins", stats.coins)
            putInt("lifetime_coins", stats.lifetimeCoins)
            putInt("day_streak", stats.dayStreak)
            putString("last_streak_date", stats.lastStreakDate)
            putInt("rounds_played", stats.roundsPlayed)
            putInt("total_correct", stats.totalCorrect)
            putInt("total_answered", stats.totalAnswered)
            putInt("highest_streak", stats.highestStreak)
            putInt("skip", stats.skipCount)
            putString("referral_code", stats.referralCode)
            putString("user_name", stats.userName)
            putString("language", stats.language)
            apply()
        }
        _userStats.value = stats
    }

    fun setLanguage(lang: String) {
        val updated = _userStats.value.copy(language = lang)
        saveUserStats(updated)
    }

    fun setUserName(name: String) {
        val updated = _userStats.value.copy(userName = name)
        saveUserStats(updated)
    }

    fun checkAndClaimDailyStreak(): Boolean {
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val current = _userStats.value
        if (current.lastStreakDate == today) {
            return false // Already claimed today
        }

        val newStreak = current.dayStreak + 1
        val bonusCoins = 25
        val updated = current.copy(
            coins = current.coins + bonusCoins,
            lifetimeCoins = current.lifetimeCoins + bonusCoins,
            dayStreak = newStreak,
            lastStreakDate = today
        )
        saveUserStats(updated)
        addActivity("Daily Streak Bonus", "Claimed Day $newStreak bonus", +bonusCoins, "bonus")
        return true
    }

    fun isDailyStreakClaimedToday(): Boolean {
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        return _userStats.value.lastStreakDate == today
    }

    fun onQuizRoundCompleted(
        modeName: String,
        correctCount: Int,
        totalQuestions: Int,
        coinsEarned: Int,
        maxStreakInRound: Int
    ) {
        val current = _userStats.value
        val updated = current.copy(
            coins = current.coins + coinsEarned,
            lifetimeCoins = current.lifetimeCoins + coinsEarned,
            roundsPlayed = current.roundsPlayed + 1,
            totalCorrect = current.totalCorrect + correctCount,
            totalAnswered = current.totalAnswered + totalQuestions,
            highestStreak = maxOf(current.highestStreak, maxStreakInRound)
        )
        saveUserStats(updated)
        addActivity(
            title = "$modeName Completed",
            description = "Score $correctCount/$totalQuestions • Max Streak: $maxStreakInRound",
            coinDelta = coinsEarned,
            type = "quiz"
        )
    }

    fun buyLifeline(type: LifelineType): Boolean {
        val current = _userStats.value
        if (current.coins < type.costCoins) return false

        val updated = when (type) {
            LifelineType.SKIP -> current.copy(
                coins = current.coins - type.costCoins,
                skipCount = current.skipCount + 1
            )
        }
        saveUserStats(updated)
        addActivity("Purchased Lifeline", "Bought ${type.title} lifeline", -type.costCoins, "lifeline")
        return true
    }

    fun useLifeline(type: LifelineType): Boolean {
        val current = _userStats.value
        val hasStock = when (type) {
            LifelineType.SKIP -> current.skipCount > 0
        }
        if (!hasStock) return false

        val updated = when (type) {
            LifelineType.SKIP -> current.copy(skipCount = current.skipCount - 1)
        }
        saveUserStats(updated)
        return true
    }

    fun requestRedemption(reward: RedeemReward, accountDetail: String): Boolean {
        val current = _userStats.value
        if (current.coins < reward.coinsRequired) return false

        // Deduct coins immediately (hold coins)
        val updatedStats = current.copy(coins = current.coins - reward.coinsRequired)
        saveUserStats(updatedStats)

        val dateFormatted = SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault()).format(Date())
        val newRequest = RedeemRequest(
            id = "REQ-" + UUID.randomUUID().toString().take(8).uppercase(),
            rewardId = reward.id,
            rewardTitle = reward.titleEn,
            coinsCost = reward.coinsRequired,
            payoutMethod = reward.method,
            accountDetail = accountDetail,
            dateFormatted = dateFormatted,
            timestamp = System.currentTimeMillis(),
            status = RedeemStatus.PENDING
        )

        val currentRequests = _redeemRequests.value.toMutableList()
        currentRequests.add(0, newRequest)
        saveRedeemRequests(currentRequests)

        addActivity(
            title = "Payout Request Submitted",
            description = "${reward.amountText} via ${reward.method} ($accountDetail)",
            coinDelta = -reward.coinsRequired,
            type = "redeem"
        )
        return true
    }

    fun adminDecideRedemption(requestId: String, approve: Boolean) {
        val currentRequests = _redeemRequests.value.toMutableList()
        val index = currentRequests.indexOfFirst { it.id == requestId }
        if (index == -1) return

        val item = currentRequests[index]
        if (item.status != RedeemStatus.PENDING) return

        if (approve) {
            currentRequests[index] = item.copy(status = RedeemStatus.APPROVED)
            addActivity("Payout Approved", "Payment sent for ${item.rewardTitle}", 0, "redeem")
        } else {
            currentRequests[index] = item.copy(status = RedeemStatus.REJECTED)
            // Refund coins!
            val stats = _userStats.value
            saveUserStats(stats.copy(coins = stats.coins + item.coinsCost))
            addActivity("Payout Rejected & Refunded", "${item.coinsCost} coins returned", +item.coinsCost, "redeem")
        }
        saveRedeemRequests(currentRequests)
    }

    fun grantDemoCoins(amount: Int) {
        val current = _userStats.value
        val updated = current.copy(
            coins = current.coins + amount,
            lifetimeCoins = current.lifetimeCoins + amount
        )
        saveUserStats(updated)
        addActivity("Demo Coins Granted", "+$amount test coins for redemption testing", +amount, "bonus")
    }

    fun grantAdRewardCoins(amount: Int = 20) {
        val current = _userStats.value
        val updated = current.copy(
            coins = current.coins + amount,
            lifetimeCoins = current.lifetimeCoins + amount
        )
        saveUserStats(updated)
        addActivity("Rewarded Video Ad", "+$amount coins earned from watching ad", +amount, "bonus")
    }

    private fun addActivity(title: String, description: String, coinDelta: Int, type: String) {
        val dateFormatted = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date())
        val newItem = ActivityItem(
            id = UUID.randomUUID().toString(),
            title = title,
            description = description,
            coinDelta = coinDelta,
            timestampFormatted = dateFormatted,
            type = type
        )
        val current = _activityLog.value.toMutableList()
        current.add(0, newItem)
        if (current.size > 30) current.removeAt(current.lastIndex)
        _activityLog.value = current
    }

    private fun loadRedeemRequests(): List<RedeemRequest> {
        // Provide 1 initial historical request so the queue is immediately previewable
        return listOf(
            RedeemRequest(
                id = "REQ-DEMO01",
                rewardId = "recharge_150",
                rewardTitle = "৳150 Mobile Recharge",
                coinsCost = 1000,
                payoutMethod = "Mobile Recharge",
                accountDetail = "+8801700112233",
                dateFormatted = "Yesterday, 04:30 PM",
                timestamp = System.currentTimeMillis() - 86400000,
                status = RedeemStatus.APPROVED
            )
        )
    }

    private fun saveRedeemRequests(list: List<RedeemRequest>) {
        _redeemRequests.value = list
    }

    private fun loadActivityLog(): List<ActivityItem> {
        return listOf(
            ActivityItem(
                id = "act_welcome",
                title = "Welcome Bonus",
                description = "Account created & 120 welcome coins credited",
                coinDelta = 120,
                timestampFormatted = "Today",
                type = "bonus"
            )
        )
    }

    fun getLeaderboard(): List<LeaderboardUser> {
        val currentUserCoins = _userStats.value.coins
        val currentUserName = _userStats.value.userName
        val list = mutableListOf(
            LeaderboardUser(1, "Rahim Ahmed", 12450, 96, false, 0xFF3B82F6),
            LeaderboardUser(2, "Tanvir Hasan", 9820, 92, false, 0xFF10B981),
            LeaderboardUser(3, "Anika Tabassum", 8340, 94, false, 0xFFEC4899),
            LeaderboardUser(4, "Fahim Shahriar", 6200, 89, false, 0xFFF59E0B),
            LeaderboardUser(5, "Nusrat Jahan", 5180, 88, false, 0xFF8B5CF6),
            LeaderboardUser(6, currentUserName, currentUserCoins, 90, true, 0xFFFBBF24),
            LeaderboardUser(7, "Sabbir Hossain", 3420, 84, false, 0xFF06B6D4),
            LeaderboardUser(8, "Mehedi Hasan", 2890, 81, false, 0xFF6366F1),
            LeaderboardUser(9, "Samira Khan", 1950, 79, false, 0xFF14B8A6),
            LeaderboardUser(10, "Arif Chowdhury", 1400, 75, false, 0xFFE11D48)
        )
        // Sort by coins descending
        return list.sortedByDescending { it.coins }.mapIndexed { idx, item ->
            item.copy(rank = idx + 1)
        }
    }
}
