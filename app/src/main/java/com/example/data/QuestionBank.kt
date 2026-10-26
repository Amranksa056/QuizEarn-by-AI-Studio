package com.example.data

import com.example.model.QuizCategoryInfo
import com.example.model.QuizQuestion

object QuestionBank {

    val categories = listOf(
        QuizCategoryInfo(
            id = "general",
            emoji = "🧠",
            titleEn = "General",
            titleBn = "সাধারণ",
            descriptionEn = "Everyday trivia",
            descriptionBn = "দৈনন্দিন সাধারণ জ্ঞান",
            colorHex = 0xFF3B82F6
        ),
        QuizCategoryInfo(
            id = "science",
            emoji = "🔬",
            titleEn = "Science",
            titleBn = "বিজ্ঞান",
            descriptionEn = "How the world works",
            descriptionBn = "বিশ্ব কীভাবে কাজ করে",
            colorHex = 0xFF10B981
        ),
        QuizCategoryInfo(
            id = "technology",
            emoji = "💻",
            titleEn = "Technology",
            titleBn = "প্রযুক্তি",
            descriptionEn = "Code, gadgets, web",
            descriptionBn = "কোড, গ্যাজেট ও ওয়েব",
            colorHex = 0xFF8B5CF6
        ),
        QuizCategoryInfo(
            id = "geography",
            emoji = "🌍",
            titleEn = "Geography",
            titleBn = "ভূগোল",
            descriptionEn = "Places & capitals",
            descriptionBn = "স্থান ও রাজধানীসমূহ",
            colorHex = 0xFFF59E0B
        ),
        QuizCategoryInfo(
            id = "sports",
            emoji = "🏆",
            titleEn = "Sports",
            titleBn = "খেলাধুলা",
            descriptionEn = "Games & champions",
            descriptionBn = "খেলা ও চ্যাম্পিয়ন",
            colorHex = 0xFFEC4899
        ),
        QuizCategoryInfo(
            id = "current",
            emoji = "🗞️",
            titleEn = "Current Affairs",
            titleBn = "কারেন্ট অ্যাফেয়ার্স",
            descriptionEn = "What is happen this week",
            descriptionBn = "চলতি সপ্তাহের গুরুত্বপূর্ণ তথ্য",
            colorHex = 0xFF06B6D4
        )
    )

    val allQuestions = listOf(
        // GENERAL KNOWLEDGE
        QuizQuestion(
            id = "gk_1",
            categoryId = "general",
            questionEn = "Which is the tallest mountain in the world above sea level?",
            questionBn = "সমুদ্রপৃষ্ঠের উপরে বিশ্বের সর্বোচ্চ পর্বত কোনটি?",
            optionsEn = listOf("K2", "Mount Everest", "Kangchenjunga", "Lhotse"),
            optionsBn = listOf("কে২", "মাউন্ট এভারেস্ট", "কাঞ্চনজঙ্ঘা", "লোহৎসে"),
            correctIndex = 1
        ),
        QuizQuestion(
            id = "gk_2",
            categoryId = "general",
            questionEn = "What is the primary currency of Japan?",
            questionBn = "জাপানের প্রধান মুদ্রা কোনটি?",
            optionsEn = listOf("Yuan", "Won", "Yen", "Baht"),
            optionsBn = listOf("ইউয়ান", "ওন", "ইয়েন", "বাথ"),
            correctIndex = 2
        ),
        QuizQuestion(
            id = "gk_3",
            categoryId = "general",
            questionEn = "How many colors are there in a standard rainbow?",
            questionBn = "একটি সাধারণ রংধনুতে কয়টি রঙ থাকে?",
            optionsEn = listOf("5", "6", "7", "8"),
            optionsBn = listOf("৫", "৬", "৭", "৮"),
            correctIndex = 2
        ),
        QuizQuestion(
            id = "gk_4",
            categoryId = "general",
            questionEn = "Which country gifted the Statue of Liberty to the United States?",
            questionBn = "কোন দেশ মার্কিন যুক্তরাষ্ট্রকে স্ট্যাচু অব লিবার্টি উপহার দিয়েছিল?",
            optionsEn = listOf("France", "United Kingdom", "Germany", "Canada"),
            optionsBn = listOf("ফ্রান্স", "যুক্তরাজ্য", "জার্মানি", "কানাডা"),
            correctIndex = 0
        ),
        QuizQuestion(
            id = "gk_5",
            categoryId = "general",
            questionEn = "Which instrument is used to measure atmospheric pressure?",
            questionBn = "বায়ুমণ্ডলীয় চাপ পরিমাপ করতে কোন যন্ত্র ব্যবহার করা হয়?",
            optionsEn = listOf("Thermometer", "Barometer", "Hygrometer", "Speedometer"),
            optionsBn = listOf("থার্মোমিটার", "ব্যারোমিটার", "হাইগ্রোমিটার", "স্পিডোমিটার"),
            correctIndex = 1
        ),

        // SCIENCE & NATURE
        QuizQuestion(
            id = "sci_1",
            categoryId = "science",
            questionEn = "What gas do plants absorb during photosynthesis?",
            questionBn = "সালোকসংশ্লেষণের সময় গাছপালা কোন গ্যাস গ্রহণ করে?",
            optionsEn = listOf("Oxygen", "Carbon Dioxide", "Nitrogen", "Hydrogen"),
            optionsBn = listOf("অক্সিজেন", "কার্বন ডাই অক্সাইড", "নাইট্রোজেন", "হাইড্রোজেন"),
            correctIndex = 1
        ),
        QuizQuestion(
            id = "sci_2",
            categoryId = "science",
            questionEn = "What is the hardest natural substance found on Earth?",
            questionBn = "পৃথিবীতে প্রাপ্ত সবচেয়ে শক্ত প্রাকৃতিক পদার্থ কোনটি?",
            optionsEn = listOf("Gold", "Iron", "Diamond", "Platinum"),
            optionsBn = listOf("স্বর্ণ", "লোহা", "হীরা", "প্লাটিনাম"),
            correctIndex = 2
        ),
        QuizQuestion(
            id = "sci_3",
            categoryId = "science",
            questionEn = "How many bones are there in the adult human body?",
            questionBn = "প্রাপ্তবয়স্ক মানবদেহে মোট কয়টি হাড় থাকে?",
            optionsEn = listOf("186", "206", "226", "246"),
            optionsBn = listOf("১৮৬", "২০৬", "২২৬", "২৪৬"),
            correctIndex = 1
        ),
        QuizQuestion(
            id = "sci_4",
            categoryId = "science",
            questionEn = "Which planet is known as the 'Red Planet'?",
            questionBn = "কোন গ্রহকে 'লাল গ্রহ' বলা হয়?",
            optionsEn = listOf("Venus", "Mars", "Jupiter", "Saturn"),
            optionsBn = listOf("শুক্র", "মঙ্গল", "বৃহস্পতি", "শনি"),
            correctIndex = 1
        ),
        QuizQuestion(
            id = "sci_5",
            categoryId = "science",
            questionEn = "What is the speed of light in vacuum approximately?",
            questionBn = "শূন্য মাধ্যমে আলোর গতি আনুমানিক কত?",
            optionsEn = listOf("150,000 km/s", "300,000 km/s", "450,000 km/s", "1,000,000 km/s"),
            optionsBn = listOf("১,৫০,০০০ কিমি/সে", "৩,০০,০০০ কিমি/সে", "৪,৫০,০০০ কিমি/সে", "১০,০০,০০০ কিমি/সে"),
            correctIndex = 1
        ),

        // TECHNOLOGY & AI
        QuizQuestion(
            id = "tech_1",
            categoryId = "technology",
            questionEn = "What does 'HTTP' stand for in web browsing?",
            questionBn = "ওয়েব ব্রাউজিংয়ে 'HTTP' এর পূর্ণরূপ কী?",
            optionsEn = listOf("HyperText Transfer Protocol", "HighTech Transmission Program", "Hyperlink Text Process", "Host Transfer Protocol"),
            optionsBn = listOf("HyperText Transfer Protocol", "HighTech Transmission Program", "Hyperlink Text Process", "Host Transfer Protocol"),
            correctIndex = 0
        ),
        QuizQuestion(
            id = "tech_2",
            categoryId = "technology",
            questionEn = "Who is known as the co-founder of Apple Inc. alongside Steve Wozniak?",
            questionBn = "স্টিভ ওজনিয়াকের পাশাপাশি অ্যাপল ইনকর্পোরেটেডের সহ-প্রতিষ্ঠাতা কে?",
            optionsEn = listOf("Bill Gates", "Steve Jobs", "Elon Musk", "Mark Zuckerberg"),
            optionsBn = listOf("বিল গেটস", "স্টিভ জবস", "ইলন মাস্ক", "মার্ক জাকারবার্গ"),
            correctIndex = 1
        ),
        QuizQuestion(
            id = "tech_3",
            categoryId = "technology",
            questionEn = "Which programming language is predominantly used for modern Android development?",
            questionBn = "আধুনিক অ্যান্ড্রয়েড ডেভেলপমেন্টের জন্য কোন প্রোগ্রামিং ভাষা সর্বাধিক ব্যবহৃত হয়?",
            optionsEn = listOf("Swift", "Kotlin", "Ruby", "PHP"),
            optionsBn = listOf("সুইফট", "কোটলিন", "রুবি", "পিএইচপি"),
            correctIndex = 1
        ),
        QuizQuestion(
            id = "tech_4",
            categoryId = "technology",
            questionEn = "What does 'RAM' stand for in computer hardware?",
            questionBn = "কম্পিউটার হার্ডওয়্যারে 'RAM' এর পূর্ণরূপ কী?",
            optionsEn = listOf("Read Access Memory", "Random Access Memory", "Rapid Action Module", "Real Automation Machine"),
            optionsBn = listOf("Read Access Memory", "Random Access Memory", "Rapid Action Module", "Real Automation Machine"),
            correctIndex = 1
        ),
        QuizQuestion(
            id = "tech_5",
            categoryId = "technology",
            questionEn = "In artificial intelligence, what does 'LLM' stand for?",
            questionBn = "কৃত্রিম বুদ্ধিমত্তায় 'LLM' এর পূর্ণরূপ কী?",
            optionsEn = listOf("Large Language Model", "Linear Logic Machine", "Linked Learning Method", "Local Layer Matrix"),
            optionsBn = listOf("Large Language Model", "Linear Logic Machine", "Linked Learning Method", "Local Layer Matrix"),
            correctIndex = 0
        ),

        // GEOGRAPHY
        QuizQuestion(
            id = "geo_1",
            categoryId = "geography",
            questionEn = "What is the capital city of Australia?",
            questionBn = "অস্ট্রেলিয়ার রাজধানী শহর কোনটি?",
            optionsEn = listOf("Sydney", "Melbourne", "Canberra", "Brisbane"),
            optionsBn = listOf("সিডনি", "মেলবোর্ন", "ক্যানবেরা", "ব্রিসবেন"),
            correctIndex = 2
        ),
        QuizQuestion(
            id = "geo_2",
            categoryId = "geography",
            questionEn = "Which is the longest river in the world?",
            questionBn = "বিশ্বের দীর্ঘতম নদী কোনটি?",
            optionsEn = listOf("Amazon", "Nile", "Yangtze", "Mississippi"),
            optionsBn = listOf("আমাজন", "নীল নদ", "ইয়াংসে", "মিসিসিপি"),
            correctIndex = 1
        ),
        QuizQuestion(
            id = "geo_3",
            categoryId = "geography",
            questionEn = "Which ocean is the largest by surface area?",
            questionBn = "আয়তনে বিশ্বের বৃহত্তম মহাসাগর কোনটি?",
            optionsEn = listOf("Atlantic Ocean", "Indian Ocean", "Pacific Ocean", "Arctic Ocean"),
            optionsBn = listOf("আটলান্টিক মহাসাগর", "ভারত মহাসাগর", "প্রশান্ত মহাসাগর", "উত্তর মহাসাগর"),
            correctIndex = 2
        ),
        QuizQuestion(
            id = "geo_4",
            categoryId = "geography",
            questionEn = "Which country has the largest land area in the world?",
            questionBn = "আয়তনের দিক থেকে বিশ্বের বৃহত্তম দেশ কোনটি?",
            optionsEn = listOf("Canada", "China", "Russia", "United States"),
            optionsBn = listOf("কানাডা", "চীন", "রাশিয়া", "মার্কিন যুক্তরাষ্ট্র"),
            correctIndex = 2
        ),
        QuizQuestion(
            id = "geo_5",
            categoryId = "geography",
            questionEn = "Mount Fuji is located in which country?",
            questionBn = "মাউন্ট ফুজি কোন দেশে অবস্থিত?",
            optionsEn = listOf("South Korea", "Japan", "China", "Nepal"),
            optionsBn = listOf("দক্ষিণ কোরিয়া", "জাপান", "চীন", "নেপাল"),
            correctIndex = 1
        ),

        // SPORTS & GAMES
        QuizQuestion(
            id = "spo_1",
            categoryId = "sports",
            questionEn = "Which country won the FIFA Men's World Cup in 2022?",
            questionBn = "২০২২ সালের ফিফা পুরুষ বিশ্বকাপ কোন দেশ জিতেছে?",
            optionsEn = listOf("France", "Argentina", "Brazil", "Germany"),
            optionsBn = listOf("ফ্রান্স", "আর্জেন্টিনা", "ব্রাজিল", "জার্মানি"),
            correctIndex = 1
        ),
        QuizQuestion(
            id = "spo_2",
            categoryId = "sports",
            questionEn = "How many players are on the field for one team in a cricket match?",
            questionBn = "একটি ক্রিকেট ম্যাচে একটি দলের কতজন খেলোয়াড় মাঠে থাকেন?",
            optionsEn = listOf("9", "10", "11", "12"),
            optionsBn = listOf("৯", "১০", "১১", "১২"),
            correctIndex = 2
        ),
        QuizQuestion(
            id = "spo_3",
            categoryId = "sports",
            questionEn = "In tennis, what term represents a score of zero?",
            questionBn = "টেনিসে শূন্য স্কোরের জন্য কোন শব্দটি ব্যবহৃত হয়?",
            optionsEn = listOf("Nil", "Zero", "Love", "Blank"),
            optionsBn = listOf("নিল", "জিরো", "লাভ", "ব্ল্যাঙ্ক"),
            correctIndex = 2
        ),
        QuizQuestion(
            id = "spo_4",
            categoryId = "sports",
            questionEn = "How many rings are in the official Olympic Games symbol?",
            questionBn = "অলিম্পিক গেমসের অফিশিয়াল প্রতীকে কয়টি রিং থাকে?",
            optionsEn = listOf("4", "5", "6", "7"),
            optionsBn = listOf("৪", "৫", "৬", "৭"),
            correctIndex = 1
        ),
        QuizQuestion(
            id = "spo_5",
            categoryId = "sports",
            questionEn = "Usain Bolt is a legendary world record holder in which sport?",
            questionBn = "উসাইন বোল্ট কোন খেলার একজন কিংবদন্তি বিশ্ব রেকর্ডধারী?",
            optionsEn = listOf("Swimming", "Sprinting", "Long Jump", "Cycling"),
            optionsBn = listOf("সাঁতার", "স্প্রিন্ট (দৌড়)", "লং জাম্প", "সাইক্লিং"),
            correctIndex = 1
        ),

        // HISTORY & INVENTIONS
        QuizQuestion(
            id = "cur_1",
            categoryId = "current",
            questionEn = "Who invented the telephone?",
            questionBn = "টেলিফোন কে আবিষ্কার করেছিলেন?",
            optionsEn = listOf("Thomas Edison", "Alexander Graham Bell", "Nikola Tesla", "Guglielmo Marconi"),
            optionsBn = listOf("টমাস এডিসন", "আলেকজান্ডার গ্রাহাম বেল", "নিকোলা টেসলা", "গুয়েলমো মার্কনি"),
            correctIndex = 1
        ),
        QuizQuestion(
            id = "cur_2",
            categoryId = "current",
            questionEn = "In which year did the Apollo 11 moon landing take place?",
            questionBn = "অ্যাপোলো ১১ চন্দ্রাভিযান কোন সালে সংঘটিত হয়েছিল?",
            optionsEn = listOf("1965", "1969", "1972", "1975"),
            optionsBn = listOf("১৯৬৫", "১৯৬৯", "১৯৭২", "১৯৭৫"),
            correctIndex = 1
        ),
        QuizQuestion(
            id = "cur_3",
            categoryId = "current",
            questionEn = "Who was the first woman to win a Nobel Prize?",
            questionBn = "নোবেল পুরস্কার বিজয়ী প্রথম নারী কে ছিলেন?",
            optionsEn = listOf("Mother Teresa", "Marie Curie", "Jane Addams", "Rosalind Franklin"),
            optionsBn = listOf("মাদার তেরেসা", "মারি ক্যুরি", "জেন অ্যাডামস", "রোজালিন্ড ফ্র্যাঙ্কলিন"),
            correctIndex = 1
        ),
        QuizQuestion(
            id = "cur_4",
            categoryId = "current",
            questionEn = "Which ancient civilization built the Pyramids of Giza?",
            questionBn = "গিজার পিরামিড কোন প্রাচীন সভ্যতা নির্মাণ করেছিল?",
            optionsEn = listOf("Ancient Romans", "Ancient Egyptians", "Ancient Greeks", "Babylonians"),
            optionsBn = listOf("প্রাচীন রোমান", "প্রাচীন মিশরীয়", "প্রাচীন গ্রীক", "ব্যাবিলনীয়"),
            correctIndex = 1
        ),
        QuizQuestion(
            id = "cur_5",
            categoryId = "current",
            questionEn = "Who painted the Mona Lisa?",
            questionBn = "মোনালিসা চিত্রকর্মটি কার আঁকা?",
            optionsEn = listOf("Vincent van Gogh", "Leonardo da Vinci", "Pablo Picasso", "Michelangelo"),
            optionsBn = listOf("ভিনসেন্ট ভ্যান গগ", "লিওনার্দো দা ভিঞ্চি", "পাবলো পিকাসো", "মিকেলেঞ্জেলো"),
            correctIndex = 1
        ),

        // ADDITIONAL BULLET / BONUS QUESTIONS
        QuizQuestion(
            id = "extra_1",
            categoryId = "general",
            questionEn = "What is the capital city of France?",
            questionBn = "ফ্রান্সের রাজধানী শহর কোনটি?",
            optionsEn = listOf("Lyon", "Marseille", "Paris", "Nice"),
            optionsBn = listOf("লিওন", "মার্সেই", "প্যারিস", "নিস"),
            correctIndex = 2
        ),
        QuizQuestion(
            id = "extra_2",
            categoryId = "science",
            questionEn = "What is the chemical formula for water?",
            questionBn = "পানির রাসায়নিক সংকেত কোনটি?",
            optionsEn = listOf("CO2", "H2O", "O2", "NaCl"),
            optionsBn = listOf("CO2", "H2O", "O2", "NaCl"),
            correctIndex = 1
        ),
        QuizQuestion(
            id = "extra_3",
            categoryId = "technology",
            questionEn = "Who is the CEO of Tesla Motors?",
            questionBn = "টেসলা মোটরসের প্রধান নির্বাহী কে?",
            optionsEn = listOf("Jeff Bezos", "Elon Musk", "Tim Cook", "Sundar Pichai"),
            optionsBn = listOf("জেফ বেজোস", "ইলন মাস্ক", "টিম কুক", "সুন্দর পিচাই"),
            correctIndex = 1
        ),
        QuizQuestion(
            id = "extra_4",
            categoryId = "geography",
            questionEn = "Which continent contains the Sahara Desert?",
            questionBn = "সাহারা মরুভূমি কোন মহাদেশে অবস্থিত?",
            optionsEn = listOf("Asia", "Africa", "South America", "Australia"),
            optionsBn = listOf("এশিয়া", "আফ্রিকা", "দক্ষিণ আমেরিকা", "অস্ট্রেলিয়া"),
            correctIndex = 1
        ),
        QuizQuestion(
            id = "extra_5",
            categoryId = "sports",
            questionEn = "How many holes are there in a standard round of golf?",
            questionBn = "একটি স্ট্যান্ডার্ড গল্ফ রাউন্ডে কয়টি হোল থাকে?",
            optionsEn = listOf("9", "12", "18", "24"),
            optionsBn = listOf("৯", "১২", "১৮", "২৪"),
            correctIndex = 2
        ),
        QuizQuestion(
            id = "extra_6",
            categoryId = "current",
            questionEn = "Which country was the first to land a rover on the far side of the Moon?",
            questionBn = "চাঁদের দূরবর্তী অংশে প্রথম রোভার অবতরণ করায় কোন দেশ?",
            optionsEn = listOf("USA", "Russia", "China", "India"),
            optionsBn = listOf("যুক্তরাষ্ট্র", "রাশিয়া", "চীন", "ভারত"),
            correctIndex = 2
        )
    )

    fun getQuestionsForCategory(categoryId: String, count: Int = 20): List<QuizQuestion> {
        val filtered = allQuestions.filter { it.categoryId == categoryId }
        val pool = if (filtered.size < count) {
            // Fill with other questions to make a full round
            (filtered + allQuestions.filter { it.categoryId != categoryId }.shuffled()).take(count)
        } else {
            filtered.shuffled().take(count)
        }
        return pool.shuffled()
    }

    fun getBulletQuestions(count: Int = 20): List<QuizQuestion> {
        // Bullet quiz uses all categories mixed, fast-paced
        return if (allQuestions.size <= count) {
            allQuestions.shuffled()
        } else {
            allQuestions.shuffled().take(count)
        }
    }
}
