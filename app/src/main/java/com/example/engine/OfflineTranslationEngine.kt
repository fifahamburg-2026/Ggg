package com.example.engine

import com.example.data.model.TranslationResult
import java.util.Locale
import kotlin.system.measureTimeMillis

class OfflineTranslationEngine {

    // Fast indexed maps
    private val arToEnWordMap = HashMap<String, WordEntry>()
    private val enToArWordMap = HashMap<String, WordEntry>()

    private val arToEnMultiMap = HashMap<String, WordEntry>()
    private val enToArMultiMap = HashMap<String, WordEntry>()

    // Arabic transliteration lookup table for letters
    private val arPhoneticMap = mapOf(
        'ا' to "a", 'أ' to "a", 'إ' to "i", 'آ' to "aa", 'ء' to "'", 'ئ' to "'", 'ؤ' to "'",
        'ب' to "b", 'ت' to "t", 'ث' to "th", 'ج' to "j", 'ح' to "h", 'خ' to "kh",
        'د' to "d", 'ذ' to "dh", 'ر' to "r", 'ز' to "z", 'س' to "s", 'ش' to "sh",
        'ص' to "s", 'ض' to "d", 'ط' to "t", 'ظ' to "dh", 'ع' to "'a", 'غ' to "gh",
        'ف' to "f", 'ق' to "q", 'ك' to "k", 'ل' to "l", 'م' to "m", 'ن' to "n",
        'ه' to "h", 'و' to "w", 'ي' to "y", 'ى' to "a", 'ة' to "ah"
    )

    // Common English names & places transliterated
    private val commonEnglishNames = mapOf(
        "sarah" to "سارة", "sara" to "سارة", "john" to "جون", "david" to "ديفيد",
        "michael" to "مايكل", "james" to "جيمس", "robert" to "روبرت", "mary" to "ماري",
        "london" to "لندن", "paris" to "باريس", "dubai" to "دبي", "cairo" to "القاهرة",
        "riyadh" to "الرياض", "tokyo" to "طوكيو", "new york" to "نيويورك",
        "google" to "جوجل", "apple" to "أبل", "android" to "أندرويد", "samsung" to "سامسونج"
    )

    // Common Arabic names transliterated
    private val commonArabicNames = mapOf(
        "محمد" to "Mohammed", "احمد" to "Ahmad", "علي" to "Ali", "عمر" to "Omar",
        "خالد" to "Khalid", "عبدالله" to "Abdullah", "عبدالرحمن" to "Abdulrahman",
        "ساره" to "Sarah", "فاطمه" to "Fatimah", "مريم" to "Maryam", "نوره" to "Noura",
        "دبي" to "Dubai", "الرياض" to "Riyadh", "القاهره" to "Cairo", "مكه" to "Makkah",
        "المدينه" to "Madinah", "جده" to "Jeddah", "الدوحه" to "Doha", "مسقط" to "Muscat"
    )

    // English past tense irregular map
    private val englishPastTenseMap = mapOf(
        "go" to "went", "come" to "came", "see" to "saw", "take" to "took",
        "make" to "made", "give" to "gave", "get" to "got", "say" to "said",
        "tell" to "told", "think" to "thought", "know" to "knew", "buy" to "bought",
        "bring" to "brought", "eat" to "ate", "drink" to "drank", "write" to "wrote",
        "read" to "read", "speak" to "spoke", "find" to "found", "hear" to "heard",
        "feel" to "felt", "leave" to "left", "meet" to "met", "run" to "ran",
        "sit" to "sat", "stand" to "stood", "sleep" to "slept", "pay" to "paid",
        "send" to "sent", "build" to "built", "understand" to "understood",
        "help" to "helped", "work" to "worked", "play" to "played", "live" to "lived",
        "love" to "loved", "like" to "liked", "ask" to "asked", "travel" to "traveled"
    )

    init {
        indexDictionary()
    }

    private fun indexDictionary() {
        // 1. Index base words
        for (w in OfflineDictionaryData.words) {
            val normAr = normalizeArabic(w.ar)
            val normEn = normalizeEnglish(w.en)
            if (!arToEnWordMap.containsKey(normAr)) arToEnWordMap[normAr] = w
            if (!enToArWordMap.containsKey(normEn)) enToArWordMap[normEn] = w
        }

        // 2. Index comprehensive dictionary words
        for (w in ComprehensiveDictionaryData.words) {
            val normAr = normalizeArabic(w.ar)
            val normEn = normalizeEnglish(w.en)
            if (!arToEnWordMap.containsKey(normAr)) arToEnWordMap[normAr] = w
            if (!enToArWordMap.containsKey(normEn)) enToArWordMap[normEn] = w
        }

        // 3. Index multi-word phrases
        for (m in OfflineDictionaryData.multiWordPhrases) {
            val normAr = normalizeArabic(m.ar)
            val normEn = normalizeEnglish(m.en)
            arToEnMultiMap[normAr] = m
            enToArMultiMap[normEn] = m
        }

        // 4. Index phrasebook items
        for (p in OfflinePhrasebookData.phrases) {
            val normAr = normalizeArabic(p.arabic)
            val normEn = normalizeEnglish(p.english)
            val entry = WordEntry(
                ar = p.arabic,
                en = p.english,
                pos = "expr",
                phonetic = p.phonetic,
                synonyms = listOf(p.context)
            )
            if (!arToEnMultiMap.containsKey(normAr)) arToEnMultiMap[normAr] = entry
            if (!enToArMultiMap.containsKey(normEn)) enToArMultiMap[normEn] = entry
        }
    }

    /**
     * Translates input text from sourceLang to targetLang.
     * If sourceLang is "auto", automatically detects the source language.
     */
    fun translate(
        rawText: String,
        requestedSourceLang: String = "auto",
        requestedTargetLang: String = "auto"
    ): TranslationResult {
        val trimmed = rawText.trim()
        if (trimmed.isEmpty()) {
            return TranslationResult(
                translatedText = "",
                phonetic = "",
                detectedSourceLang = if (requestedSourceLang == "auto") "ar" else requestedSourceLang,
                detectedTargetLang = if (requestedTargetLang == "auto") "en" else requestedTargetLang,
                processingTimeMs = 0
            )
        }

        val sourceLang: String
        val targetLang: String

        if (requestedSourceLang == "auto") {
            sourceLang = detectLanguage(trimmed)
            targetLang = if (sourceLang == "ar") "en" else "ar"
        } else {
            sourceLang = requestedSourceLang
            targetLang = if (requestedTargetLang != "auto") requestedTargetLang else if (sourceLang == "ar") "en" else "ar"
        }

        var result: TranslationResult
        val timeMs = measureTimeMillis {
            result = if (sourceLang == "ar") {
                translateArabicToEnglish(trimmed)
            } else {
                translateEnglishToArabic(trimmed)
            }
        }

        return result.copy(
            detectedSourceLang = sourceLang,
            detectedTargetLang = targetLang,
            processingTimeMs = timeMs
        )
    }

    /**
     * Detects if the text is predominantly Arabic or English.
     */
    fun detectLanguage(text: String): String {
        var arabicCount = 0
        var latinCount = 0
        for (ch in text) {
            val code = ch.code
            if (code in 0x0600..0x06FF || code in 0x0750..0x077F || code in 0x08A0..0x08FF) {
                arabicCount++
            } else if ((ch in 'a'..'z') || (ch in 'A'..'Z')) {
                latinCount++
            }
        }
        return if (arabicCount >= latinCount) "ar" else "en"
    }

    // ==========================================
    // ARABIC -> ENGLISH TRANSLATION
    // ==========================================

    private fun translateArabicToEnglish(input: String): TranslationResult {
        val normalizedInput = normalizeArabic(input)

        // 1. Direct match on multi-word phrases or phrasebook
        arToEnMultiMap[normalizedInput]?.let { entry ->
            return TranslationResult(
                translatedText = capitalizeFirst(entry.en),
                phonetic = entry.phonetic,
                detectedSourceLang = "ar",
                detectedTargetLang = "en",
                partOfSpeech = entry.pos,
                alternativeMeanings = entry.synonyms,
                isExactMatch = true
            )
        }

        // 2. Direct single word match
        lookupSingleArabicWord(normalizedInput)?.let { match ->
            return TranslationResult(
                translatedText = capitalizeFirst(match.translated),
                phonetic = match.phonetic.ifEmpty { generateArabicPhonetic(input) },
                detectedSourceLang = "ar",
                detectedTargetLang = "en",
                partOfSpeech = match.pos,
                alternativeMeanings = match.synonyms,
                isExactMatch = true
            )
        }

        // 3. Sentence / multi-token translation
        val tokens = tokenize(input)
        val translatedTokens = mutableListOf<String>()
        val alternativeList = mutableListOf<String>()
        var foundAny = false
        var dominantPos = ""

        var i = 0
        while (i < tokens.size) {
            val token = tokens[i]

            if (!isWordToken(token)) {
                translatedTokens.add(token)
                i++
                continue
            }

            // Greedy phrase check (up to 4 tokens)
            var matchedPhrase = false
            for (len in minOf(4, tokens.size - i) downTo 2) {
                val subTokens = tokens.subList(i, i + len).filter { isWordToken(it) }
                val phraseCandidate = normalizeArabic(subTokens.joinToString(" "))
                val phraseEntry = arToEnMultiMap[phraseCandidate]
                if (phraseEntry != null) {
                    translatedTokens.add(phraseEntry.en)
                    if (dominantPos.isEmpty()) dominantPos = phraseEntry.pos
                    foundAny = true
                    i += len
                    matchedPhrase = true
                    break
                }
            }
            if (matchedPhrase) continue

            // Check Arabic Negation Compounds:
            // "لا" / "لم" / "لن" / "ما" + verb
            val normToken = normalizeArabic(token)
            if ((normToken == "لا" || normToken == "لم" || normToken == "لن" || normToken == "ما") && i + 1 < tokens.size && isWordToken(tokens[i + 1])) {
                val nextNorm = normalizeArabic(tokens[i + 1])
                val verbMatch = lookupSingleArabicWord(nextNorm)
                if (verbMatch != null) {
                    val negationPrefix = when (normToken) {
                        "لم", "ما" -> "did not "
                        "لن" -> "will not "
                        else -> "do not "
                    }
                    translatedTokens.add(negationPrefix + verbMatch.translated.removePrefix("I ").removePrefix("we ").removePrefix("he ").removePrefix("she "))
                    foundAny = true
                    i += 2
                    continue
                }
            }

            // Single word translation with morphology
            val wordMatch = lookupSingleArabicWord(normToken)
            if (wordMatch != null) {
                translatedTokens.add(wordMatch.translated)
                if (dominantPos.isEmpty()) dominantPos = wordMatch.pos
                if (wordMatch.synonyms.isNotEmpty()) {
                    alternativeList.addAll(wordMatch.synonyms.take(2))
                }
                foundAny = true
            } else {
                // Fallback: transliterate proper names or unknown tokens
                val transliterated = transliterateArabicToEnglish(normToken)
                translatedTokens.add(transliterated)
            }
            i++
        }

        val assembledTranslation = assembleEnglishSentence(translatedTokens)

        return TranslationResult(
            translatedText = capitalizeFirst(assembledTranslation),
            phonetic = generateArabicPhonetic(input),
            detectedSourceLang = "ar",
            detectedTargetLang = "en",
            partOfSpeech = dominantPos,
            alternativeMeanings = alternativeList.distinct().take(3),
            isExactMatch = foundAny && tokens.size <= 2
        )
    }

    private data class ArabicLookupResult(
        val translated: String,
        val pos: String = "n",
        val phonetic: String = "",
        val synonyms: List<String> = emptyList()
    )

    private fun lookupSingleArabicWord(normWord: String): ArabicLookupResult? {
        // Direct dictionary match
        arToEnWordMap[normWord]?.let { entry ->
            return ArabicLookupResult(entry.en, entry.pos, entry.phonetic, entry.synonyms)
        }

        // Check common names
        commonArabicNames[normWord]?.let { name ->
            return ArabicLookupResult(name, "n", name)
        }

        // 1. Check feminine ending -ة / -ه (e.g. كبيرة -> كبير, جميلة -> جميل, سعيدة -> سعيد)
        if (normWord.endsWith("ه") && normWord.length > 2) {
            val base = normWord.dropLast(1)
            arToEnWordMap[base]?.let { entry ->
                return ArabicLookupResult(entry.en, entry.pos, entry.phonetic, entry.synonyms)
            }
        }

        // 2. Check future verb prefixes (سأ, سي, ست, سن, س)
        if (normWord.startsWith("سا") && normWord.length > 3) {
            val stem = normWord.substring(2)
            lookupVerbStem(stem)?.let { verb ->
                return ArabicLookupResult("I will " + verb.en, "v", verb.phonetic)
            }
        }
        if (normWord.startsWith("سي") && normWord.length > 3) {
            val stem = normWord.substring(2)
            lookupVerbStem(stem)?.let { verb ->
                return ArabicLookupResult("he will " + verb.en, "v", verb.phonetic)
            }
        }
        if (normWord.startsWith("ست") && normWord.length > 3) {
            val stem = normWord.substring(2)
            lookupVerbStem(stem)?.let { verb ->
                return ArabicLookupResult("she will " + verb.en, "v", verb.phonetic)
            }
        }
        if (normWord.startsWith("سن") && normWord.length > 3) {
            val stem = normWord.substring(2)
            lookupVerbStem(stem)?.let { verb ->
                return ArabicLookupResult("we will " + verb.en, "v", verb.phonetic)
            }
        }

        // 3. Present tense verb prefixes: أ, ي, ت, ن
        if (normWord.startsWith("ي") && normWord.length > 3) {
            val stem = normWord.substring(1)
            lookupVerbStem(stem)?.let { verb ->
                return ArabicLookupResult(verb.en + "s", "v", verb.phonetic)
            }
        }
        if (normWord.startsWith("ت") && normWord.length > 3) {
            val stem = normWord.substring(1)
            lookupVerbStem(stem)?.let { verb ->
                return ArabicLookupResult(verb.en, "v", verb.phonetic)
            }
        }
        if (normWord.startsWith("ا") && normWord.length > 3) {
            val stem = normWord.substring(1)
            lookupVerbStem(stem)?.let { verb ->
                return ArabicLookupResult("I " + verb.en, "v", verb.phonetic)
            }
        }
        if (normWord.startsWith("ن") && normWord.length > 3) {
            val stem = normWord.substring(1)
            lookupVerbStem(stem)?.let { verb ->
                return ArabicLookupResult("we " + verb.en, "v", verb.phonetic)
            }
        }

        // 4. Check common prefixes (conjunctions, prepositions, definite article)
        val prefixRules = listOf(
            PrefixRule("وال", "and the "),
            PrefixRule("وبال", "and with the "),
            PrefixRule("وكال", "and like the "),
            PrefixRule("ولل", "and for the "),
            PrefixRule("فال", "so the "),
            PrefixRule("فل", "so for the "),
            PrefixRule("بال", "in the "),
            PrefixRule("كال", "like the "),
            PrefixRule("لل", "for the "),
            PrefixRule("ال", "the "),
            PrefixRule("و", "and "),
            PrefixRule("ف", "so "),
            PrefixRule("ب", "with "),
            PrefixRule("ك", "as "),
            PrefixRule("ل", "for "),
            PrefixRule("س", "will ")
        )

        for (rule in prefixRules) {
            if (normWord.startsWith(rule.prefix) && normWord.length > rule.prefix.length + 2) {
                val stem = normWord.substring(rule.prefix.length)
                arToEnWordMap[stem]?.let { entry ->
                    return ArabicLookupResult(
                        translated = rule.enPrefix + entry.en,
                        pos = entry.pos,
                        phonetic = entry.phonetic,
                        synonyms = entry.synonyms
                    )
                }

                // Check feminine after prefix
                if (stem.endsWith("ه") && stem.length > 2) {
                    val base = stem.dropLast(1)
                    arToEnWordMap[base]?.let { entry ->
                        return ArabicLookupResult(
                            translated = rule.enPrefix + entry.en,
                            pos = entry.pos,
                            phonetic = entry.phonetic
                        )
                    }
                }

                // Also try stem with suffix stripping
                lookupSuffix(stem)?.let { suffixMatch ->
                    return ArabicLookupResult(
                        translated = rule.enPrefix + suffixMatch.translated,
                        pos = suffixMatch.pos,
                        phonetic = suffixMatch.phonetic
                    )
                }
            }
        }

        // 5. Check suffix without prefix (possessives, verb past suffixes, plurals)
        lookupSuffix(normWord)?.let { suffixMatch ->
            return suffixMatch
        }

        return null
    }

    private fun lookupVerbStem(stem: String): WordEntry? {
        arToEnWordMap[stem]?.let { if (it.pos == "v" || it.pos == "n") return it }
        arToEnWordMap[stem + "ه"]?.let { if (it.pos == "v") return it }
        return null
    }

    private fun lookupSuffix(stem: String): ArabicLookupResult? {
        // Verb past tense suffixes:
        // -ت (I/you wrote), -نا (we wrote), -وا (they wrote), -تم (you all wrote)
        if (stem.endsWith("ت") && stem.length > 3) {
            val base = stem.dropLast(1)
            lookupVerbStem(base)?.let { v ->
                return ArabicLookupResult(pastTenseEnglish(v.en), "v", v.phonetic)
            }
        }
        if (stem.endsWith("نا") && stem.length > 3) {
            val base = stem.dropLast(2)
            lookupVerbStem(base)?.let { v ->
                return ArabicLookupResult("we " + pastTenseEnglish(v.en), "v", v.phonetic)
            }
        }
        if (stem.endsWith("وا") && stem.length > 3) {
            val base = stem.dropLast(2)
            lookupVerbStem(base)?.let { v ->
                return ArabicLookupResult("they " + pastTenseEnglish(v.en), "v", v.phonetic)
            }
        }

        // Verb object pronouns:
        // -ني (me: ساعدني -> help me)
        if (stem.endsWith("ني") && stem.length > 3) {
            val base = stem.dropLast(2)
            arToEnWordMap[base]?.let { entry ->
                return ArabicLookupResult(entry.en + " me", entry.pos, entry.phonetic)
            }
        }

        val suffixRules = listOf(
            SuffixRule("هما", "their "),
            SuffixRule("هم", "their "),
            SuffixRule("هن", "their "),
            SuffixRule("كم", "your "),
            SuffixRule("كن", "your "),
            SuffixRule("نا", "our "),
            SuffixRule("ها", "her "),
            SuffixRule("ه", "his "),
            SuffixRule("ي", "my "),
            SuffixRule("ك", "your "),
            SuffixRule("ات", "", isPlural = true),
            SuffixRule("ين", "", isPlural = true),
            SuffixRule("ون", "", isPlural = true),
            SuffixRule("ان", "two ", isDual = true)
        )

        for (rule in suffixRules) {
            if (stem.endsWith(rule.suffix) && stem.length > rule.suffix.length + 2) {
                val base = stem.substring(0, stem.length - rule.suffix.length)
                val candidates = listOf(base, base + "ه", base + "ة")
                for (cand in candidates) {
                    arToEnWordMap[cand]?.let { entry ->
                        val translatedWord = when {
                            rule.isPlural -> pluralizeEnglish(entry.en)
                            rule.isDual -> "two " + pluralizeEnglish(entry.en)
                            entry.pos == "v" -> entry.en + " " + rule.enPrefix.trim()
                            else -> rule.enPrefix + entry.en
                        }
                        return ArabicLookupResult(translatedWord, entry.pos, entry.phonetic)
                    }
                }
            }
        }
        return null
    }

    private fun pastTenseEnglish(verb: String): String {
        englishPastTenseMap[verb]?.let { return it }
        return when {
            verb.endsWith("e") -> verb + "d"
            verb.endsWith("y") && !verb.endsWith("ay") && !verb.endsWith("ey") && !verb.endsWith("oy") ->
                verb.dropLast(1) + "ied"
            else -> verb + "ed"
        }
    }

    // ==========================================
    // ENGLISH -> ARABIC TRANSLATION
    // ==========================================

    private fun translateEnglishToArabic(input: String): TranslationResult {
        val expandedInput = expandEnglishContractions(input)
        val normalizedInput = normalizeEnglish(expandedInput)

        // 1. Exact phrase match
        enToArMultiMap[normalizedInput]?.let { entry ->
            return TranslationResult(
                translatedText = entry.ar,
                phonetic = entry.phonetic.ifEmpty { generateArabicPhonetic(entry.ar) },
                detectedSourceLang = "en",
                detectedTargetLang = "ar",
                partOfSpeech = entry.pos,
                alternativeMeanings = entry.synonyms,
                isExactMatch = true
            )
        }

        // 2. Direct single word match
        lookupSingleEnglishWord(normalizedInput)?.let { match ->
            return TranslationResult(
                translatedText = match.translated,
                phonetic = match.phonetic.ifEmpty { generateArabicPhonetic(match.translated) },
                detectedSourceLang = "en",
                detectedTargetLang = "ar",
                partOfSpeech = match.pos,
                alternativeMeanings = match.synonyms,
                isExactMatch = true
            )
        }

        // 3. Sentence / phrasal translation
        val tokens = tokenize(expandedInput)
        val translatedTokens = mutableListOf<String>()
        val alternativeList = mutableListOf<String>()
        var foundAny = false
        var dominantPos = ""

        var i = 0
        while (i < tokens.size) {
            val token = tokens[i]

            if (!isWordToken(token)) {
                translatedTokens.add(token)
                i++
                continue
            }

            // Check multi-word expression (up to 4 tokens)
            var matchedPhrase = false
            for (len in minOf(4, tokens.size - i) downTo 2) {
                val subTokens = tokens.subList(i, i + len).filter { isWordToken(it) }
                val phraseCandidate = normalizeEnglish(subTokens.joinToString(" "))
                val phraseEntry = enToArMultiMap[phraseCandidate]
                if (phraseEntry != null) {
                    translatedTokens.add(phraseEntry.ar)
                    if (dominantPos.isEmpty()) dominantPos = phraseEntry.pos
                    foundAny = true
                    i += len
                    matchedPhrase = true
                    break
                }
            }
            if (matchedPhrase) continue

            // Check English [Adjective + Noun] inversion rule
            // Example: "big house" -> in English Adj=big, Noun=house -> In Arabic: "بيت كبير" (Noun + Adj)
            if (i + 1 < tokens.size && isWordToken(tokens[i]) && isWordToken(tokens[i + 1])) {
                val word1 = normalizeEnglish(tokens[i])
                val word2 = normalizeEnglish(tokens[i + 1])
                val match1 = lookupSingleEnglishWord(word1)
                val match2 = lookupSingleEnglishWord(word2)

                if (match1 != null && match2 != null && match1.pos == "adj" && match2.pos == "n") {
                    val arabicNoun = match2.translated
                    val arabicAdj = match1.translated
                    translatedTokens.add("$arabicNoun $arabicAdj")
                    foundAny = true
                    i += 2
                    continue
                }
            }

            // Single word lookup with morphology
            val normToken = normalizeEnglish(token)
            val match = lookupSingleEnglishWord(normToken)
            if (match != null) {
                translatedTokens.add(match.translated)
                if (dominantPos.isEmpty()) dominantPos = match.pos
                if (match.synonyms.isNotEmpty()) {
                    alternativeList.addAll(match.synonyms.take(2))
                }
                foundAny = true
            } else {
                // Fallback: transliterate English names or unknown tokens
                val transliterated = transliterateEnglishToArabic(normToken)
                translatedTokens.add(transliterated)
            }
            i++
        }

        val assembledTranslation = assembleArabicSentence(translatedTokens)

        return TranslationResult(
            translatedText = assembledTranslation,
            phonetic = generateArabicPhonetic(assembledTranslation),
            detectedSourceLang = "en",
            detectedTargetLang = "ar",
            partOfSpeech = dominantPos,
            alternativeMeanings = alternativeList.distinct().take(3),
            isExactMatch = foundAny && tokens.size <= 2
        )
    }

    private data class EnglishLookupResult(
        val translated: String,
        val pos: String = "n",
        val phonetic: String = "",
        val synonyms: List<String> = emptyList()
    )

    private fun lookupSingleEnglishWord(normWord: String): EnglishLookupResult? {
        // Direct dictionary match
        enToArWordMap[normWord]?.let { entry ->
            return EnglishLookupResult(entry.ar, entry.pos, entry.phonetic, entry.synonyms)
        }

        // Common names & places
        commonEnglishNames[normWord]?.let { arName ->
            return EnglishLookupResult(arName, "n")
        }

        // Check irregular forms
        OfflineDictionaryData.irregularEnglishVerbs[normWord]?.let { baseVerb ->
            enToArWordMap[baseVerb]?.let { entry ->
                return EnglishLookupResult(entry.ar, "v", entry.phonetic, entry.synonyms)
            }
        }

        OfflineDictionaryData.irregularPlurals[normWord]?.let { baseNoun ->
            enToArWordMap[baseNoun]?.let { entry ->
                return EnglishLookupResult(entry.ar, "n", entry.phonetic, entry.synonyms)
            }
        }

        // Suffix -ing (verbs or gerunds)
        if (normWord.endsWith("ing") && normWord.length > 4) {
            val stem = normWord.removeSuffix("ing")
            val candidates = listOf(stem, stem + "e", stem.dropLast(1))
            for (c in candidates) {
                enToArWordMap[c]?.let { entry ->
                    return EnglishLookupResult(entry.ar, "v", entry.phonetic)
                }
            }
        }

        // Suffix -ed (past tense)
        if (normWord.endsWith("ed") && normWord.length > 3) {
            val stem = normWord.removeSuffix("ed")
            val candidates = listOf(
                stem,
                stem + "d",
                stem + "e",
                if (stem.endsWith("i")) stem.dropLast(1) + "y" else stem,
                stem.dropLast(1) // stopped -> stop
            )
            for (c in candidates) {
                enToArWordMap[c]?.let { entry ->
                    return EnglishLookupResult(entry.ar, "v", entry.phonetic)
                }
            }
        }

        // Suffix -s / -es / -ies (plurals or 3rd person singular)
        if (normWord.endsWith("ies") && normWord.length > 4) {
            val stem = normWord.removeSuffix("ies") + "y"
            enToArWordMap[stem]?.let { entry ->
                return EnglishLookupResult(entry.ar, entry.pos, entry.phonetic)
            }
        }
        if (normWord.endsWith("es") && normWord.length > 3) {
            val stem = normWord.removeSuffix("es")
            enToArWordMap[stem]?.let { entry ->
                return EnglishLookupResult(entry.ar, entry.pos, entry.phonetic)
            }
        }
        if (normWord.endsWith("s") && normWord.length > 2) {
            val stem = normWord.removeSuffix("s")
            enToArWordMap[stem]?.let { entry ->
                return EnglishLookupResult(entry.ar, entry.pos, entry.phonetic)
            }
        }

        // Suffix -ly (adverbs from adjectives)
        if (normWord.endsWith("ly") && normWord.length > 3) {
            val stem = normWord.removeSuffix("ly")
            val candidates = listOf(stem, stem + "e", stem.dropLast(1) + "y")
            for (c in candidates) {
                enToArWordMap[c]?.let { entry ->
                    val arAdv = when (c) {
                        "quick" -> "بسرعة"
                        "slow" -> "ببطء"
                        "easy" -> "بسهولة"
                        "careful" -> "بحذر"
                        "clear" -> "بوضوح"
                        "quiet" -> "بهدوء"
                        "exact" -> "بالضبط"
                        "perfect" -> "بمثالية"
                        "safe" -> "بأمان"
                        "direct" -> "مباشرة"
                        "real" -> "حقا"
                        else -> "بشكل " + entry.ar
                    }
                    return EnglishLookupResult(arAdv, "adv", entry.phonetic)
                }
            }
        }

        // Suffix -er (comparative)
        if (normWord.endsWith("er") && normWord.length > 3) {
            val stem = normWord.removeSuffix("er")
            val candidates = listOf(stem, stem + "e", stem.dropLast(1))
            for (c in candidates) {
                enToArWordMap[c]?.let { entry ->
                    if (entry.pos == "adj") {
                        return EnglishLookupResult("أكثر " + entry.ar, "adj", entry.phonetic)
                    }
                }
            }
        }

        return null
    }

    // ==========================================
    // UTILITIES & NORMALIZATION
    // ==========================================

    fun normalizeArabic(text: String): String {
        return text
            .replace(Regex("[\u064B-\u065F\u0670]"), "") // Remove tashkeel
            .replace(Regex("[أإآٱ]"), "ا")               // Normalize Alef
            .replace('ة', 'ه')                          // Normalize Ta Marbouta
            .replace('ى', 'ي')                          // Normalize Alif Maqsura
            .replace(Regex("[\\p{Punct}&&[^']]"), "")   // Remove punctuation
            .trim()
            .lowercase()
    }

    fun normalizeEnglish(text: String): String {
        return text
            .lowercase(Locale.ENGLISH)
            .replace(Regex("[\\p{Punct}&&[^']]"), "")
            .trim()
    }

    private fun expandEnglishContractions(text: String): String {
        var result = text.lowercase(Locale.ENGLISH)
        for ((contraction, expansion) in OfflineDictionaryData.contractions) {
            result = result.replace(Regex("\\b$contraction\\b"), expansion)
        }
        return result
    }

    private fun tokenize(text: String): List<String> {
        val result = mutableListOf<String>()
        val matcher = Regex("([\\p{L}\\p{N}]+|[^\\p{L}\\p{N}\\s]+|\\s+)").findAll(text)
        for (match in matcher) {
            result.add(match.value)
        }
        return result
    }

    private fun isWordToken(token: String): Boolean {
        return token.any { it.isLetter() || it.isDigit() }
    }

    private fun assembleEnglishSentence(tokens: List<String>): String {
        val sb = StringBuilder()
        for (i in tokens.indices) {
            val t = tokens[i]
            if (t.isBlank()) {
                if (sb.isNotEmpty() && !sb.endsWith(" ")) sb.append(" ")
            } else if (isPunctuation(t)) {
                sb.append(t).append(" ")
            } else {
                if (sb.isNotEmpty() && !sb.endsWith(" ") && !sb.endsWith("'")) {
                    sb.append(" ")
                }
                sb.append(t)
            }
        }
        return sb.toString().trim().replace(Regex("\\s+"), " ")
    }

    private fun assembleArabicSentence(tokens: List<String>): String {
        val sb = StringBuilder()
        for (i in tokens.indices) {
            val t = tokens[i]
            if (t.isBlank()) {
                if (sb.isNotEmpty() && !sb.endsWith(" ")) sb.append(" ")
            } else if (isPunctuation(t)) {
                sb.append(t).append(" ")
            } else {
                if (sb.isNotEmpty() && !sb.endsWith(" ")) {
                    sb.append(" ")
                }
                sb.append(t)
            }
        }
        return sb.toString().trim().replace(Regex("\\s+"), " ")
    }

    private fun isPunctuation(token: String): Boolean {
        return token.length == 1 && (token[0] in ".,!?;:؟،؛")
    }

    private fun capitalizeFirst(text: String): String {
        if (text.isEmpty()) return text
        return text.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ENGLISH) else it.toString() }
    }

    private fun pluralizeEnglish(word: String): String {
        return when {
            word.endsWith("y") && !word.endsWith("ay") && !word.endsWith("ey") && !word.endsWith("oy") ->
                word.dropLast(1) + "ies"
            word.endsWith("s") || word.endsWith("sh") || word.endsWith("ch") || word.endsWith("x") || word.endsWith("z") ->
                word + "es"
            else -> word + "s"
        }
    }

    /**
     * Generates a phonetic transliteration for Arabic text to assist pronunciation.
     */
    fun generateArabicPhonetic(arabicText: String): String {
        val sb = StringBuilder()
        for (ch in arabicText) {
            val latin = arPhoneticMap[ch]
            if (latin != null) {
                sb.append(latin)
            } else if (ch.isWhitespace() || ch in ".,!?") {
                sb.append(ch)
            }
        }
        val result = sb.toString().trim().replace(Regex("\\s+"), " ")
        return capitalizeFirst(result)
    }

    /**
     * Transliterates unknown Arabic word or proper name to English characters.
     */
    private fun transliterateArabicToEnglish(arWord: String): String {
        commonArabicNames[arWord]?.let { return it }
        val sb = StringBuilder()
        for (ch in arWord) {
            val mapped = arPhoneticMap[ch]
            if (mapped != null) {
                sb.append(mapped)
            } else {
                sb.append(ch)
            }
        }
        return capitalizeFirst(sb.toString())
    }

    /**
     * Transliterates unknown English word or proper name to Arabic letters.
     */
    private fun transliterateEnglishToArabic(enWord: String): String {
        commonEnglishNames[enWord.lowercase(Locale.ENGLISH)]?.let { return it }

        var w = enWord.lowercase(Locale.ENGLISH)
        val sb = StringBuilder()
        var idx = 0
        while (idx < w.length) {
            if (idx + 1 < w.length) {
                val pair = w.substring(idx, idx + 2)
                when (pair) {
                    "sh" -> { sb.append("ش"); idx += 2; continue }
                    "ch" -> { sb.append("تش"); idx += 2; continue }
                    "th" -> { sb.append("ث"); idx += 2; continue }
                    "kh" -> { sb.append("خ"); idx += 2; continue }
                    "gh" -> { sb.append("غ"); idx += 2; continue }
                    "ph" -> { sb.append("ف"); idx += 2; continue }
                    "ee", "ea" -> { sb.append("ي"); idx += 2; continue }
                    "oo" -> { sb.append("و"); idx += 2; continue }
                    "ou" -> { sb.append("او"); idx += 2; continue }
                }
            }

            val ch = w[idx]
            val arChar = when (ch) {
                'a' -> if (idx == 0) "أ" else "ا"
                'b' -> "ب"
                'c' -> "ك"
                'd' -> "د"
                'e' -> if (idx == 0) "إ" else "ي"
                'f' -> "ف"
                'g' -> "ج"
                'h' -> "ه"
                'i' -> if (idx == 0) "إ" else "ي"
                'j' -> "ج"
                'k' -> "ك"
                'l' -> "ل"
                'm' -> "م"
                'n' -> "ن"
                'o' -> "و"
                'p' -> "ب"
                'q' -> "ق"
                'r' -> "ر"
                's' -> "س"
                't' -> "ت"
                'u' -> "و"
                'v' -> "ف"
                'w' -> "و"
                'x' -> "اكس"
                'y' -> "ي"
                'z' -> "ز"
                else -> ch.toString()
            }
            sb.append(arChar)
            idx++
        }
        return sb.toString()
    }

    // Rule data classes
    private data class PrefixRule(val prefix: String, val enPrefix: String)
    private data class SuffixRule(val suffix: String, val enPrefix: String = "", val isPlural: Boolean = false, val isDual: Boolean = false)
}
