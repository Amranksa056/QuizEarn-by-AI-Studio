package com.example.model

enum class QuizMode(val displayNameEn: String, val displayNameBn: String) {
    BULLET("Bullet Quiz", "বুলেট কুইজ"),
    CATEGORY("Category Round", "ক্যাটাগরি রাউন্ড")
}

enum class LifelineType(val title: String, val costCoins: Int) {
    SKIP("Skip", 50)
}

enum class RedeemStatus {
    PENDING,
    APPROVED,
    REJECTED
}

data class QuizQuestion(
    val id: String,
    val categoryId: String,
    val questionEn: String,
    val questionBn: String,
    val optionsEn: List<String>,
    val optionsBn: List<String>,
    val correctIndex: Int,
    val explanationEn: String = "",
    val explanationBn: String = ""
)

data class QuizCategoryInfo(
    val id: String,
    val emoji: String,
    val titleEn: String,
    val titleBn: String,
    val descriptionEn: String,
    val descriptionBn: String,
    val colorHex: Long
)

data class RedeemReward(
    val id: String,
    val titleEn: String,
    val titleBn: String,
    val coinsRequired: Int,
    val amountText: String,
    val currency: String,
    val method: String, // bKash, Nagad, Mobile Recharge, PayPal
    val subtitle: String
)

data class RedeemRequest(
    val id: String,
    val rewardId: String,
    val rewardTitle: String,
    val coinsCost: Int,
    val payoutMethod: String,
    val accountDetail: String,
    val dateFormatted: String,
    val timestamp: Long,
    val status: RedeemStatus
)

data class ActivityItem(
    val id: String,
    val title: String,
    val description: String,
    val coinDelta: Int,
    val timestampFormatted: String,
    val type: String // "quiz", "bullet", "bonus", "redeem", "lifeline"
)

data class UserStats(
    val coins: Int = 120, // Initial welcome coins
    val lifetimeCoins: Int = 120,
    val dayStreak: Int = 1,
    val lastStreakDate: String = "",
    val roundsPlayed: Int = 0,
    val totalCorrect: Int = 0,
    val totalAnswered: Int = 0,
    val highestStreak: Int = 0,
    val skipCount: Int = 3,
    val referralCode: String = "QE" + (1000..9999).random(),
    val userName: String = "QuizMaster",
    val language: String = "en" // "en" or "bn"
)

data class LeaderboardUser(
    val rank: Int,
    val name: String,
    val coins: Int,
    val accuracy: Int,
    val isCurrentUser: Boolean = false,
    val avatarBgHex: Long
)
