package com.example.engine

import com.example.data.model.PhraseCategory
import com.example.data.model.PhraseItem

object OfflinePhrasebookData {
    val phrases: List<PhraseItem> = listOf(
        // GREETINGS & BASICS
        PhraseItem("gr_1", PhraseCategory.GREETINGS, "مرحباً", "Hello", "Marhaban", "تحية عامة"),
        PhraseItem("gr_2", PhraseCategory.GREETINGS, "السلام عليكم", "Peace be upon you", "As-salamu alaykum", "تحية إسلامية رسمية"),
        PhraseItem("gr_3", PhraseCategory.GREETINGS, "وعليكم السلام", "And upon you be peace", "Wa alaykumu s-salam", "الرد على السلام"),
        PhraseItem("gr_4", PhraseCategory.GREETINGS, "صباح الخير", "Good morning", "Sabah al-khayr", "تحية الصباح"),
        PhraseItem("gr_5", PhraseCategory.GREETINGS, "صباح النور", "Good morning (reply)", "Sabah an-nur", "الرد على صباح الخير"),
        PhraseItem("gr_6", PhraseCategory.GREETINGS, "مساء الخير", "Good evening", "Masa' al-khayr", "تحية المساء"),
        PhraseItem("gr_7", PhraseCategory.GREETINGS, "مساء النور", "Good evening (reply)", "Masa' an-nur", "الرد على مساء الخير"),
        PhraseItem("gr_8", PhraseCategory.GREETINGS, "كيف حالك؟", "How are you?", "Kayfa haluk?", "سؤال عن الحال"),
        PhraseItem("gr_9", PhraseCategory.GREETINGS, "أنا بخير، شكراً لك", "I am fine, thank you", "Ana bikhayr, shukran lak", "إجابة عن الحال"),
        PhraseItem("gr_10", PhraseCategory.GREETINGS, "ما اسمك؟", "What is your name?", "Ma ismuk?", "سؤال عن الاسم"),
        PhraseItem("gr_11", PhraseCategory.GREETINGS, "اسمي هو...", "My name is...", "Ismi huwa...", "التعريف بالنفس"),
        PhraseItem("gr_12", PhraseCategory.GREETINGS, "تشرفت بمعرفتك", "Pleased to meet you", "Tasharraftu bima'rifatik", "عند اللقاء لأول مرة"),
        PhraseItem("gr_13", PhraseCategory.GREETINGS, "مع السلامة", "Goodbye", "Ma'a as-salama", "عند الوداع"),
        PhraseItem("gr_14", PhraseCategory.GREETINGS, "إلى اللقاء قريباً", "See you soon", "Ila al-liqa' qariban", "وداع ودي"),
        PhraseItem("gr_15", PhraseCategory.GREETINGS, "شكراً جزيلاً", "Thank you very much", "Shukran jazilan", "شكر وامتنان"),
        PhraseItem("gr_16", PhraseCategory.GREETINGS, "عفواً / على الرحب والسعة", "You are welcome", "'Afwan / 'Ala ar-rahbi was-sa'ah", "الرد على الشكر"),
        PhraseItem("gr_17", PhraseCategory.GREETINGS, "من فضلك / لو سمحت", "Please", "Min fadlik / Law samaht", "طلب مهذب"),
        PhraseItem("gr_18", PhraseCategory.GREETINGS, "أنا آسف", "I am sorry", "Ana asif", "اعتذار"),
        PhraseItem("gr_19", PhraseCategory.GREETINGS, "المعذرة / عذراً", "Excuse me", "Al-ma'dhira / 'Udhran", "لفت انتباه أو اعتذار"),
        PhraseItem("gr_20", PhraseCategory.GREETINGS, "نعم / لا", "Yes / No", "Na'am / La", "تأكيد أو نفي"),
        PhraseItem("gr_21", PhraseCategory.GREETINGS, "هل تتحدث الإنجليزية؟", "Do you speak English?", "Hal tatakallam al-injiliziyyah?", "لغة التخاطب"),
        PhraseItem("gr_22", PhraseCategory.GREETINGS, "أنا أتحدث العربية فقط", "I only speak Arabic", "Ana atakallam al-'arabiyyah faqat", "توضيح لغوي"),
        PhraseItem("gr_23", PhraseCategory.GREETINGS, "لا أفهم جيداً", "I don't understand well", "La afhamu jayyidan", "صعوبة فهم"),
        PhraseItem("gr_24", PhraseCategory.GREETINGS, "هل يمكنك تكرار ذلك؟", "Can you repeat that?", "Hal yumkinuka takrar dhalik?", "طلب إعادة"),

        // TRAVEL & TRANSIT
        PhraseItem("tr_1", PhraseCategory.TRAVEL, "أين المطار؟", "Where is the airport?", "Ayna al-matar?", "المواصلات الجوية"),
        PhraseItem("tr_2", PhraseCategory.TRAVEL, "محطة القطار", "Train station", "Mahattat al-qitar", "سفر بالقطار"),
        PhraseItem("tr_3", PhraseCategory.TRAVEL, "محطة الحافلات", "Bus station", "Mahattat al-hafilat", "المواصلات العامة"),
        PhraseItem("tr_4", PhraseCategory.TRAVEL, "أين يمكنني إيجاد سيارة أجرة؟", "Where can I find a taxi?", "Ayna yumkinuni ijadu sayyarat ujrah?", "طلب تاكسي"),
        PhraseItem("tr_5", PhraseCategory.TRAVEL, "كم تكلفة الأجرة إلى وسط المدينة؟", "How much is the fare to downtown?", "Kam taklufat al-ujrah ila wasat al-madinah?", "سعر الرحلة"),
        PhraseItem("tr_6", PhraseCategory.TRAVEL, "أريد الذهاب إلى هذا العنوان", "I want to go to this address", "Uridu adh-dhahaba ila hadha al-'unwan", "إرشاد السائق"),
        PhraseItem("tr_7", PhraseCategory.TRAVEL, "توقف هنا من فضلك", "Stop here, please", "Tawaqqaf huna min fadlik", "طلب النزول"),
        PhraseItem("tr_8", PhraseCategory.TRAVEL, "أين أقرب محطة مترو؟", "Where is the nearest metro station?", "Ayna aqrabu mahattat metro?", "مترو الأنفاق"),
        PhraseItem("tr_9", PhraseCategory.TRAVEL, "انعطف يميناً", "Turn right", "In'atif yaminan", "توجيهات"),
        PhraseItem("tr_10", PhraseCategory.TRAVEL, "انعطف يساراً", "Turn left", "In'atif yasaran", "توجيهات"),
        PhraseItem("tr_11", PhraseCategory.TRAVEL, "امشِ للأمام مباشرة", "Go straight ahead", "Imshi lil-amam mubasharatan", "توجيهات"),
        PhraseItem("tr_12", PhraseCategory.TRAVEL, "أين جواز سفري؟", "Where is my passport?", "Ayna jawaz safari?", "وثائق السفر"),
        PhraseItem("tr_13", PhraseCategory.TRAVEL, "متى موعد الرحلة؟", "What time is the flight?", "Mata maw'id ar-rihlah?", "مواعيد الطيران"),
        PhraseItem("tr_14", PhraseCategory.TRAVEL, "تذكرة ذهاب وإياب", "Round-trip ticket", "Tadhkirat dhahab wa-iyab", "شراء تذاكر"),
        PhraseItem("tr_15", PhraseCategory.TRAVEL, "تذكرة ذهاب فقط", "One-way ticket", "Tadhkirat dhahab faqat", "شراء تذاكر"),
        PhraseItem("tr_16", PhraseCategory.TRAVEL, "أين صالة المغادرة؟", "Where is the departure hall?", "Ayna salat al-mughadarah?", "المطار"),
        PhraseItem("tr_17", PhraseCategory.TRAVEL, "أين بوابة الصعود؟", "Where is the boarding gate?", "Ayna bawwabat as-su'ud?", "المطار"),
        PhraseItem("tr_18", PhraseCategory.TRAVEL, "حقائبي مفقودة", "My baggage is missing", "Haqa'ibi mafqudah", "فقدان الأمتعة"),

        // DINING & FOOD
        PhraseItem("dn_1", PhraseCategory.DINING, "طاولة لشخصين من فضلك", "A table for two, please", "Tawilah li-shakhsayn min fadlik", "حجز طاولة"),
        PhraseItem("dn_2", PhraseCategory.DINING, "هل لديكم قائمة طعام؟", "Do you have a menu?", "Hal ladaykum qa'imat ta'am?", "قائمة الوجبات"),
        PhraseItem("dn_3", PhraseCategory.DINING, "أريد كوب ماء من فضلك", "I want a cup of water, please", "Uridu kub ma' min fadlik", "طلب مشروب"),
        PhraseItem("dn_4", PhraseCategory.DINING, "ما هو الطبق الموصى به هنا؟", "What is the recommended dish here?", "Ma huwa at-tabaq al-mawsa bihi huna?", "استفسار عن الطعام"),
        PhraseItem("dn_5", PhraseCategory.DINING, "أنا نباتي", "I am vegetarian", "Ana nabati", "حمية غذائية"),
        PhraseItem("dn_6", PhraseCategory.DINING, "هل هذا حلال؟", "Is this Halal?", "Hal hadha halal?", "طعام حلال"),
        PhraseItem("dn_7", PhraseCategory.DINING, "بدون سكر من فضلك", "Without sugar, please", "Bidun sukkar min fadlik", "مشروبات"),
        PhraseItem("dn_8", PhraseCategory.DINING, "قهوة وشاي", "Coffee and tea", "Qahwah wa shay", "مشروبات ساخنة"),
        PhraseItem("dn_9", PhraseCategory.DINING, "الفاتورة / الحساب لو سمحت", "The bill / check, please", "Al-faturah / Al-hisab law samaht", "دفع الحساب"),
        PhraseItem("dn_10", PhraseCategory.DINING, "الطعام لذيذ جداً", "The food is delicious", "At-ta'amu ladhidh jiddan", "مدح الطعام"),
        PhraseItem("dn_11", PhraseCategory.DINING, "هل تقبلون الدفع بالبطاقة؟", "Do you accept card payment?", "Hal taqbalun ad-daf' bil-bitaqah?", "طريقة الدفع"),
        PhraseItem("dn_12", PhraseCategory.DINING, "لدي حساسية من الفول السوداني", "I am allergic to peanuts", "Ladayya hasasiyyah min al-ful as-sudani", "حساسية طعام"),
        PhraseItem("dn_13", PhraseCategory.DINING, "وجبة الإفطار", "Breakfast", "Wajbat al-iftar", "وجبات"),
        PhraseItem("dn_14", PhraseCategory.DINING, "وجبة الغداء", "Lunch", "Wajbat al-ghada'", "وجبات"),
        PhraseItem("dn_15", PhraseCategory.DINING, "وجبة العشاء", "Dinner", "Wajbat al-'asha'", "وجبات"),

        // SHOPPING & MONEY
        PhraseItem("sh_1", PhraseCategory.SHOPPING, "بكم هذا؟ / كم سعر هذا؟", "How much is this?", "Bikam hadha? / Kam si'r hadha?", "سؤال عن السعر"),
        PhraseItem("sh_2", PhraseCategory.SHOPPING, "هذا غالٍ جداً", "This is very expensive", "Hadha ghalin jiddan", "مفاوضة السعر"),
        PhraseItem("sh_3", PhraseCategory.SHOPPING, "هل يمكن تخفيض السعر؟", "Can you give a discount?", "Hal yumkin takhfid as-si'r?", "طلب خصم"),
        PhraseItem("sh_4", PhraseCategory.SHOPPING, "أريد مقاساً أكبر", "I want a larger size", "Uridu miqasan akbar", "مقاسات"),
        PhraseItem("sh_5", PhraseCategory.SHOPPING, "أريد مقاساً أصغر", "I want a smaller size", "Uridu miqasan asghar", "مقاسات"),
        PhraseItem("sh_6", PhraseCategory.SHOPPING, "هل لديك ألوان أخرى؟", "Do you have other colors?", "Hal ladayka alwan ukhra?", "اختيار ألوان"),
        PhraseItem("sh_7", PhraseCategory.SHOPPING, "أنا فقط أتفرج، شكراً", "I am just looking, thank you", "Ana faqat atafarraj, shukran", "في المتجر"),
        PhraseItem("sh_8", PhraseCategory.SHOPPING, "سآخذ هذا", "I will take this", "Sa-akhudh hadha", "شراء"),
        PhraseItem("sh_9", PhraseCategory.SHOPPING, "أين ماكينة الصراف الآلي؟", "Where is the ATM?", "Ayna makinatu as-sarraf al-ali?", "سحب نقود"),
        PhraseItem("sh_10", PhraseCategory.SHOPPING, "أين يمكنني تحويل العملة؟", "Where can I exchange money?", "Ayna yumkinuni tahwil al-'umlah?", "صرافة"),
        PhraseItem("sh_11", PhraseCategory.SHOPPING, "هل تعطيني إيصالاً؟", "Can you give me a receipt?", "Hal tu'tini i'salan?", "إثبات شراء"),
        PhraseItem("sh_12", PhraseCategory.SHOPPING, "سأدفع نقداً", "I will pay in cash", "Sa-adfa'u naqdan", "دفع نقدي"),

        // EMERGENCY & HEALTH
        PhraseItem("em_1", PhraseCategory.EMERGENCY, "ساعدني! / النجدة!", "Help me!", "Sa'idni! / An-najdah!", "طلب استغاثة"),
        PhraseItem("em_2", PhraseCategory.EMERGENCY, "اتصل بالشرطة فوراً!", "Call the police immediately!", "Ittasil bish-shurtah fawran!", "طوارئ أمنية"),
        PhraseItem("em_3", PhraseCategory.EMERGENCY, "اتصل بالإسعاف!", "Call an ambulance!", "Ittasil bil-is'af!", "طوارئ طبية"),
        PhraseItem("em_4", PhraseCategory.EMERGENCY, "أحتاج إلى طبيب", "I need a doctor", "Ahtaju ila tabib", "رعاية صحية"),
        PhraseItem("em_5", PhraseCategory.EMERGENCY, "أين أقرب مستشفى؟", "Where is the nearest hospital?", "Ayna aqrabu mustashfa?", "طوارئ طبية"),
        PhraseItem("em_6", PhraseCategory.EMERGENCY, "أين أقرب صيدلية؟", "Where is the nearest pharmacy?", "Ayna aqrabu saydaliyyah?", "شراء دواء"),
        PhraseItem("em_7", PhraseCategory.EMERGENCY, "أشعر بألم شديد هنا", "I feel severe pain here", "Ash'uru bi-alamin shadidin huna", "وصف ألم"),
        PhraseItem("em_8", PhraseCategory.EMERGENCY, "عندي حمى وصداع", "I have a fever and headache", "'Indi humma wa-suda'", "أعراض مرضية"),
        PhraseItem("em_9", PhraseCategory.EMERGENCY, "لقد فقدت محفظتي", "I lost my wallet", "Laqad faqadtu mahfazati", "فقدان متعلقات"),
        PhraseItem("em_10", PhraseCategory.EMERGENCY, "أنا تائه، هل يمكنك مساعدتي؟", "I am lost, can you help me?", "Ana ta'ih, hal yumkinuka musa'adati?", "ضياع الطريق"),
        PhraseItem("em_11", PhraseCategory.EMERGENCY, "حريق! اخرجوا بسرعة!", "Fire! Get out quickly!", "Hariq! Ukhruju bisur'ah!", "إنذار حريق"),
        PhraseItem("em_12", PhraseCategory.EMERGENCY, "أحتاج مسكن ألم", "I need a painkiller", "Ahtaju musakkin alam", "صيدلية"),

        // HOTEL & ACCOMMODATION
        PhraseItem("ht_1", PhraseCategory.HOTEL, "لدي حجز باسم...", "I have a reservation under the name...", "Ladayya hajz bi-ism...", "تسجيل الدخول"),
        PhraseItem("ht_2", PhraseCategory.HOTEL, "أريد تسجيل الوصول", "I would like to check in", "Uridu tasjil al-wusul", "فندق"),
        PhraseItem("ht_3", PhraseCategory.HOTEL, "أريد تسجيل المغادرة", "I would like to check out", "Uridu tasjil al-mughadarah", "فندق"),
        PhraseItem("ht_4", PhraseCategory.HOTEL, "ما هي كلمة سر الواي فاي؟", "What is the Wi-Fi password?", "Ma hiya kalimat sirr al-wifi?", "إنترنت"),
        PhraseItem("ht_5", PhraseCategory.HOTEL, "مفتاح الغرفة لا يعمل", "The room key is not working", "Miftah al-ghurfah la ya'mal", "مشكلة بالفندق"),
        PhraseItem("ht_6", PhraseCategory.HOTEL, "هل الإفطار مشمول في الحجز؟", "Is breakfast included?", "Hal al-iftar mashmul fil-hajz?", "خدمات الفندق"),
        PhraseItem("ht_7", PhraseCategory.HOTEL, "أريد مناشف إضافية من فضلك", "I need extra towels, please", "Uridu manashif idhafiyyah min fadlik", "خدمة الغرف"),
        PhraseItem("ht_8", PhraseCategory.HOTEL, "مكيف الهواء لا يعمل", "The air conditioner is not working", "Mukayyif al-hawa' la ya'mal", "صيانة الغرفة"),

        // WORK & BUSINESS
        PhraseItem("bs_1", PhraseCategory.BUSINESS, "سعيد بلقائك للعمل معاً", "Pleased to work with you", "Sa'idun biliqa'ik lil-'amal ma'an", "أعمال"),
        PhraseItem("bs_2", PhraseCategory.BUSINESS, "متى سيبدأ الاجتماع؟", "When will the meeting start?", "Mata sayabda' al-ijtima'?", "اجتماعات"),
        PhraseItem("bs_3", PhraseCategory.BUSINESS, "سأرسل لك بريداً إلكترونياً", "I will send you an email", "Sa-ursilu laka baridan iliktroniyyan", "مراسلات"),
        PhraseItem("bs_4", PhraseCategory.BUSINESS, "هل يمكنك توقيع هذا العقد؟", "Can you sign this contract?", "Hal yumkinuka tawqi' hadha al-'aqd?", "عقود"),
        PhraseItem("bs_5", PhraseCategory.BUSINESS, "دعنا نناقش المشروع غداً", "Let's discuss the project tomorrow", "Da'na nunaqish al-mashru' ghadan", "مشاريع"),
        PhraseItem("bs_6", PhraseCategory.BUSINESS, "أنا متفق معك تماماً", "I completely agree with you", "Ana muttafiqun ma'aka tamamam", "نقاش عمل"),

        // NUMBERS & TIME
        PhraseItem("nm_1", PhraseCategory.NUMBERS_TIME, "كم الساعة الآن؟", "What time is it now?", "Kam as-sa'ah al-an?", "سؤال عن الوقت"),
        PhraseItem("nm_2", PhraseCategory.NUMBERS_TIME, "الساعة الواحدة تماماً", "It is one o'clock", "As-sa'ah al-wahidah tamaman", "الوقت"),
        PhraseItem("nm_3", PhraseCategory.NUMBERS_TIME, "اليوم / غداً / أمس", "Today / Tomorrow / Yesterday", "Al-yawm / Ghadan / Ams", "أيام"),
        PhraseItem("nm_4", PhraseCategory.NUMBERS_TIME, "الآن / لاحقاً", "Now / Later", "Al-an / Lahiqan", "أوقات"),
        PhraseItem("nm_5", PhraseCategory.NUMBERS_TIME, "واحد، اثنان، ثلاثة", "One, two, three", "Wahid, ithnan, thalatha", "أرقام"),
        PhraseItem("nm_6", PhraseCategory.NUMBERS_TIME, "أربعة، خمسة، ستة", "Four, five, six", "Arba'ah, khamsah, sittah", "أرقام"),
        PhraseItem("nm_7", PhraseCategory.NUMBERS_TIME, "سبعة، ثمانية، تسعة، عشرة", "Seven, eight, nine, ten", "Sab'ah, thamaniyah, tis'ah, 'asharah", "أرقام"),
        PhraseItem("nm_8", PhraseCategory.NUMBERS_TIME, "مائة / ألف", "One hundred / One thousand", "Mi'ah / Alf", "أرقام كبيرة"),
        PhraseItem("nm_9", PhraseCategory.NUMBERS_TIME, "صباحاً / مساءً", "AM / PM (Morning / Evening)", "Sabahan / Masa'an", "توقيت"),
        PhraseItem("nm_10", PhraseCategory.NUMBERS_TIME, "كم من الوقت سيستغرق ذلك؟", "How long will that take?", "Kam mina al-waqt sayastaghriq dhalik?", "مدة زمنية")
    )
}
