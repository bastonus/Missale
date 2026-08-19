package com.example.liturgy.data

import android.content.Context
import com.example.liturgy.model.LiturgicalSeason
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject

data class JgabcChantItem(
    val id: String,
    val title: String,
    val part: String,
    val mode: String,
    val biblicalRef: String,
    val gabc: String,
    val gregobaseId: Int = 0
) {
    fun toProperChant(): ProperChant {
        val cleanPart = when {
            part.contains("Introit", ignoreCase = true) -> "Intr."
            part.contains("Gradual", ignoreCase = true) -> "Grad."
            part.contains("Alleluia", ignoreCase = true) -> "All."
            part.contains("Tract", ignoreCase = true) -> "Tract."
            part.contains("Sequen", ignoreCase = true) -> "Seq."
            part.contains("Offertor", ignoreCase = true) -> "Offert."
            part.contains("Commun", ignoreCase = true) -> "Comm."
            else -> part
        }
        return ProperChant(
            part = cleanPart,
            latinTitle = title,
            vernacularTitle = biblicalRef.ifBlank { title },
            mode = mode.removePrefix("Mode "),
            gabc = gabc,
            translation = biblicalRef
        )
    }
}

data class JgabcFeastSet(
    val key: String,
    val title: String,
    val subtitle: String,
    val seasonName: String,
    val chants: List<JgabcChantItem>,
    val epistleRef: String = "",
    val epistleLatin: String = "",
    val epistleVernacular: String = "",
    val gospelRef: String = "",
    val gospelLatin: String = "",
    val gospelVernacular: String = "",
    val collectLatin: String = "",
    val collectVernacular: String = "",
    val secretLatin: String = "",
    val secretVernacular: String = "",
    val postcommunionLatin: String = "",
    val postcommunionVernacular: String = ""
) {
    fun toProperSet(): ProperSet {
        val intro = chants.firstOrNull { it.part.contains("Introit", ignoreCase = true) }?.toProperChant()
            ?: chants.firstOrNull()?.toProperChant()
            ?: fallbackChant("Intr.", "Incipit")
        val grad = chants.firstOrNull { it.part.contains("Gradual", ignoreCase = true) }?.toProperChant()
            ?: fallbackChant("Grad.", "Graduale")
        val allelu = chants.firstOrNull { it.part.contains("Alleluia", ignoreCase = true) || it.part.contains("Tract", ignoreCase = true) }?.toProperChant()
            ?: fallbackChant("All.", "Alleluia")
        val seq = chants.firstOrNull { it.part.contains("Sequen", ignoreCase = true) }?.toProperChant()
        val off = chants.firstOrNull { it.part.contains("Offertor", ignoreCase = true) }?.toProperChant()
            ?: fallbackChant("Offert.", "Offertorium")
        val comm = chants.firstOrNull { it.part.contains("Commun", ignoreCase = true) }?.toProperChant()
            ?: fallbackChant("Comm.", "Communio")

        val litSeason = when {
            seasonName.contains("Advent", ignoreCase = true) || key.startsWith("Adv") -> LiturgicalSeason.ADVENT
            seasonName.contains("Christ", ignoreCase = true) || key.startsWith("Dec") || key.startsWith("Nat") -> LiturgicalSeason.CHRISTMASTIDE
            seasonName.contains("Epi", ignoreCase = true) -> LiturgicalSeason.TIME_AFTER_EPIPHANY
            seasonName.contains("Sept", ignoreCase = true) || key.contains("7a") || key.contains("6a") || key.contains("5a") -> LiturgicalSeason.SEPTUAGESIMA
            seasonName.contains("Quad", ignoreCase = true) || seasonName.contains("Lent", ignoreCase = true) || key.startsWith("Quad") -> LiturgicalSeason.LENT
            seasonName.contains("Pass", ignoreCase = true) -> LiturgicalSeason.PASSIONTIDE
            seasonName.contains("Easter", ignoreCase = true) || seasonName.contains("Pasch", ignoreCase = true) || key.startsWith("Pasc") -> LiturgicalSeason.EASTERTIDE
            seasonName.contains("Pent", ignoreCase = true) -> LiturgicalSeason.TIME_AFTER_PENTECOST
            else -> LiturgicalSeason.TIME_AFTER_PENTECOST
        }

        return ProperSet(
            id = key,
            feastLatin = title,
            feastVernacular = subtitle.ifBlank { title },
            season = litSeason,
            introit = intro,
            gradual = grad,
            alleluiaOrTract = allelu,
            sequence = seq,
            offertory = off,
            communion = comm,
            epistleRef = epistleRef,
            epistleLatin = epistleLatin,
            epistleVernacular = epistleVernacular,
            gospelRef = gospelRef,
            gospelLatin = gospelLatin,
            gospelVernacular = gospelVernacular,
            collectLatin = collectLatin,
            collectVernacular = collectVernacular,
            secretLatin = secretLatin,
            secretVernacular = secretVernacular,
            postcommunionLatin = postcommunionLatin,
            postcommunionVernacular = postcommunionVernacular
        )
    }

    private fun fallbackChant(part: String, name: String): ProperChant {
        return ProperChant(
            part = part,
            latinTitle = name,
            vernacularTitle = "",
            mode = "I",
            gabc = "name: $name;\noffice-part: $part;\nmode: 1;\n%%\n(c4) (f3)",
            translation = ""
        )
    }
}

object JgabcDatabase {
    private var cachedFeasts: List<JgabcFeastSet>? = null

    suspend fun getOrLoadAllFeasts(context: Context): List<JgabcFeastSet> {
        cachedFeasts?.let { return it }
        return withContext(Dispatchers.IO) {
            val list = mutableListOf<JgabcFeastSet>()
            try {
                val jsonString = context.assets.open("jgabc_all_propers.json").bufferedReader().use { it.readText() }
                val jsonArray = JSONArray(jsonString)
                for (i in 0 until jsonArray.length()) {
                    val obj = jsonArray.getJSONObject(i)
                    val key = obj.optString("key", "feast_$i")
                    val title = obj.optString("title", "")
                    val subtitle = obj.optString("subtitle", "")
                    val season = obj.optString("season", "")
                    val chantsArr = obj.optJSONArray("chants") ?: JSONArray()

                    val chantsList = mutableListOf<JgabcChantItem>()
                    for (j in 0 until chantsArr.length()) {
                        val cObj = chantsArr.getJSONObject(j)
                        chantsList.add(
                            JgabcChantItem(
                                id = cObj.optString("id", "${key}_$j"),
                                title = cObj.optString("title", ""),
                                part = cObj.optString("part", ""),
                                mode = cObj.optString("mode", ""),
                                biblicalRef = cObj.optString("biblicalRef", ""),
                                gabc = cObj.optString("gabc", ""),
                                gregobaseId = cObj.optInt("gregobaseId", 0)
                            )
                        )
                    }

                    list.add(
                        JgabcFeastSet(
                            key = key,
                            title = title,
                            subtitle = subtitle,
                            seasonName = season,
                            chants = chantsList,
                            epistleRef = obj.optString("epistleRef", ""),
                            epistleLatin = obj.optString("epistleLatin", ""),
                            epistleVernacular = obj.optString("epistleVernacular", ""),
                            gospelRef = obj.optString("gospelRef", ""),
                            gospelLatin = obj.optString("gospelLatin", ""),
                            gospelVernacular = obj.optString("gospelVernacular", ""),
                            collectLatin = obj.optString("collectLatin", ""),
                            collectVernacular = obj.optString("collectVernacular", ""),
                            secretLatin = obj.optString("secretLatin", ""),
                            secretVernacular = obj.optString("secretVernacular", ""),
                            postcommunionLatin = obj.optString("postcommunionLatin", ""),
                            postcommunionVernacular = obj.optString("postcommunionVernacular", "")
                        )
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            cachedFeasts = list
            list
        }
    }

    fun getAllFeastsSync(context: Context): List<JgabcFeastSet> {
        cachedFeasts?.let { return it }
        val list = mutableListOf<JgabcFeastSet>()
        try {
            val jsonString = context.assets.open("jgabc_all_propers.json").bufferedReader().use { it.readText() }
            val jsonArray = JSONArray(jsonString)
            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)
                val key = obj.optString("key", "feast_$i")
                val title = obj.optString("title", "")
                val subtitle = obj.optString("subtitle", "")
                val season = obj.optString("season", "")
                val chantsArr = obj.optJSONArray("chants") ?: JSONArray()

                val chantsList = mutableListOf<JgabcChantItem>()
                for (j in 0 until chantsArr.length()) {
                    val cObj = chantsArr.getJSONObject(j)
                    chantsList.add(
                        JgabcChantItem(
                            id = cObj.optString("id", "${key}_$j"),
                            title = cObj.optString("title", ""),
                            part = cObj.optString("part", ""),
                            mode = cObj.optString("mode", ""),
                            biblicalRef = cObj.optString("biblicalRef", ""),
                            gabc = cObj.optString("gabc", ""),
                            gregobaseId = cObj.optInt("gregobaseId", 0)
                        )
                    )
                }

                list.add(
                    JgabcFeastSet(
                        key = key,
                        title = title,
                        subtitle = subtitle,
                        seasonName = season,
                        chants = chantsList,
                        epistleRef = obj.optString("epistleRef", ""),
                        epistleLatin = obj.optString("epistleLatin", ""),
                        epistleVernacular = obj.optString("epistleVernacular", ""),
                        gospelRef = obj.optString("gospelRef", ""),
                        gospelLatin = obj.optString("gospelLatin", ""),
                        gospelVernacular = obj.optString("gospelVernacular", ""),
                        collectLatin = obj.optString("collectLatin", ""),
                        collectVernacular = obj.optString("collectVernacular", ""),
                        secretLatin = obj.optString("secretLatin", ""),
                        secretVernacular = obj.optString("secretVernacular", ""),
                        postcommunionLatin = obj.optString("postcommunionLatin", ""),
                        postcommunionVernacular = obj.optString("postcommunionVernacular", "")
                    )
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        cachedFeasts = list
        return list
    }
}
