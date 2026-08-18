package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoFixHigh
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.liturgy.data.KyrialeLibrary
import com.example.liturgy.data.PropersLibrary
import com.example.liturgy.gabc.GregorianScoreView
import com.example.ui.theme.RubricRed

@Composable
fun ChantsScreen(
    modifier: Modifier = Modifier
) {
    var selectedSection by remember { mutableIntStateOf(0) } // 0: Propres de la Messe, 1: Kyriale & Ordinaire, 2: Éditeur GABC officiel
    val sections = listOf("Propres de la Messe", "Kyriale & Ordinaire", "Éditeur GABC Officiel")

    var selectedProperIndex by remember { mutableIntStateOf(0) }
    val properSets = PropersLibrary.allSets

    var selectedCategory by remember { mutableStateOf("Tous") }
    val categories = listOf("Tous", "Asperges", "Kyrie", "Sanctus", "Agnus Dei", "Credo")

    // Official GABC Custom Editor State
    var customGabcText by remember {
        mutableStateOf(
            """name: Asperges me;
office-part: Antiphona;
mode: 7;
book: Graduale Romanum, 1961, p. 1* & The Liber Usualis, 1961, p. 11;
transcriber: Andrew Hinkley;
commentary: Ps 50: 9;
%%
(c2) A(cd)spér(fe)ges(d) me,(ef) *(,) Dó(g)mi(f)ne,(f) hys(fd)só(f)po,(g) et(f) mun(e)dá(d)bor :(d.) (:) la(cd)vá(fe)bis(d) me,(ef) (,) et(g) su(f)per(f) ni(fd)vem(f) de(g)al(f)bá(e)bor.(d.) (::)
(z) <sp>V/</sp>. Mi(h)se(gh)ré(h)re(h) me(h)i,(h) De(hg)us,(gh..) *(:) se(h)cún(h)dum(h) ma(h)gnam(h) mi(h)se(h)ri(h)cór(h)di(h)am(g) tu(h)am.(h.) (::)"""
        )
    }

    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 8.dp)
            .testTag("chants_screen_list"),
        contentPadding = PaddingValues(top = 8.dp, bottom = 80.dp)
    ) {
        // Top Header Banner
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp, horizontal = 4.dp)
            ) {
                Text(
                    text = "Graduale & Kyriale Romanum (GABC Officiel)",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Format officiel Gregorio / GregoBase avec en-têtes canoniques, notation Solesmes sur 4 lignes, donneur de ton et éditeur interactif.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
        }

        // Section Tabs
        item {
            ScrollableTabRow(
                selectedTabIndex = selectedSection,
                edgePadding = 0.dp,
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
            ) {
                sections.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedSection == index,
                        onClick = { selectedSection = index },
                        text = { Text(title, fontWeight = if (selectedSection == index) FontWeight.Bold else FontWeight.Normal) },
                        modifier = Modifier.testTag("chants_tab_$index")
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }

        if (selectedSection == 0) {
            // PROPRES DE LA MESSE (Official GABC format)
            item {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    items(properSets.indices.toList()) { index ->
                        val set = properSets[index]
                        FilterChip(
                            selected = selectedProperIndex == index,
                            onClick = { selectedProperIndex = index },
                            label = { Text(set.feastVernacular.split("(").first().trim()) },
                            modifier = Modifier.testTag("proper_chip_$index")
                        )
                    }
                }
                Spacer(modifier = Modifier.height(6.dp))
            }

            val currentSet = properSets[selectedProperIndex]

            item {
                Text(
                    text = currentSet.feastLatin,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(vertical = 4.dp, horizontal = 4.dp)
                )
            }

            // 1. Introitus
            item {
                GregorianScoreView(
                    rawGabc = currentSet.introit.gabc,
                    title = "${currentSet.introit.part} - ${currentSet.introit.latinTitle}",
                    mode = currentSet.introit.mode,
                    translation = currentSet.introit.translation,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }

            // 2. Graduale
            item {
                GregorianScoreView(
                    rawGabc = currentSet.gradual.gabc,
                    title = "${currentSet.gradual.part} - ${currentSet.gradual.latinTitle}",
                    mode = currentSet.gradual.mode,
                    translation = currentSet.gradual.translation,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }

            // 3. Alleluia / Tractus
            item {
                GregorianScoreView(
                    rawGabc = currentSet.alleluiaOrTract.gabc,
                    title = "${currentSet.alleluiaOrTract.part} - ${currentSet.alleluiaOrTract.latinTitle}",
                    mode = currentSet.alleluiaOrTract.mode,
                    translation = currentSet.alleluiaOrTract.translation,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }

            // 3b. Sequentia (si présente)
            if (currentSet.sequence != null) {
                item {
                    GregorianScoreView(
                        rawGabc = currentSet.sequence.gabc,
                        title = "${currentSet.sequence.part} - ${currentSet.sequence.latinTitle}",
                        mode = currentSet.sequence.mode,
                        translation = currentSet.sequence.translation,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
            }

            // 4. Offertorium
            item {
                GregorianScoreView(
                    rawGabc = currentSet.offertory.gabc,
                    title = "${currentSet.offertory.part} - ${currentSet.offertory.latinTitle}",
                    mode = currentSet.offertory.mode,
                    translation = currentSet.offertory.translation,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }

            // 5. Communio
            item {
                GregorianScoreView(
                    rawGabc = currentSet.communion.gabc,
                    title = "${currentSet.communion.part} - ${currentSet.communion.latinTitle}",
                    mode = currentSet.communion.mode,
                    translation = currentSet.communion.translation,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        } else if (selectedSection == 1) {
            // KYRIALE & ORDINAIRE (Official GABC format)
            item {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    items(categories) { category ->
                        FilterChip(
                            selected = selectedCategory == category,
                            onClick = { selectedCategory = category },
                            label = { Text(category) },
                            modifier = Modifier.testTag("filter_chip_$category")
                        )
                    }
                }
                Spacer(modifier = Modifier.height(6.dp))
            }

            val filteredChants = if (selectedCategory == "Tous") {
                KyrialeLibrary.allChants
            } else {
                KyrialeLibrary.allChants.filter { it.part.equals(selectedCategory, ignoreCase = true) }
            }

            items(filteredChants) { chant ->
                GregorianScoreView(
                    rawGabc = chant.gabc,
                    title = chant.title,
                    mode = chant.mode,
                    translation = chant.translation,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        } else {
            // ÉDITEUR & LECTEUR GABC OFFICIEL
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.background
                    ),
                    border = androidx.compose.foundation.BorderStroke(1.dp, RubricRed.copy(alpha = 0.35f)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.MusicNote,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = "Lecteur & Éditeur GABC officiel",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Collez ou saisissez du code GABC standard avec en-têtes pour un rendu immédiat :",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Sample Templates Quick Load Row
                        LazyRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            item {
                                OutlinedButton(
                                    onClick = { customGabcText = KyrialeLibrary.aspergesMe.gabc },
                                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                                ) {
                                    Text("Asperges me", style = MaterialTheme.typography.labelSmall)
                                }
                            }
                            item {
                                OutlinedButton(
                                    onClick = { customGabcText = KyrialeLibrary.kyrieDeAngelis.gabc },
                                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                                ) {
                                    Text("Kyrie VIII", style = MaterialTheme.typography.labelSmall)
                                }
                            }
                            item {
                                OutlinedButton(
                                    onClick = { customGabcText = KyrialeLibrary.sanctusDeAngelis.gabc },
                                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                                ) {
                                    Text("Sanctus VIII", style = MaterialTheme.typography.labelSmall)
                                }
                            }
                            item {
                                OutlinedButton(
                                    onClick = { customGabcText = PropersLibrary.easterSunday.introit.gabc },
                                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                                ) {
                                    Text("Resurrexi", style = MaterialTheme.typography.labelSmall)
                                }
                            }
                            item {
                                OutlinedButton(
                                    onClick = { customGabcText = PropersLibrary.christmasDay.introit.gabc },
                                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                                ) {
                                    Text("Puer natus", style = MaterialTheme.typography.labelSmall)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Text Field for GABC Code
                        OutlinedTextField(
                            value = customGabcText,
                            onValueChange = { customGabcText = it },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(160.dp)
                                .testTag("custom_gabc_input"),
                            label = { Text("Code source GABC officiel") },
                            textStyle = TextStyle(fontFamily = FontFamily.Monospace, fontSize = 12.sp)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            OutlinedButton(
                                onClick = { customGabcText = "" },
                                modifier = Modifier.padding(end = 8.dp)
                            ) {
                                Icon(Icons.Default.Clear, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Effacer")
                            }

                            Button(
                                onClick = {
                                    clipboardManager.setText(AnnotatedString(customGabcText))
                                    Toast.makeText(context, "Code GABC officiel copié !", Toast.LENGTH_SHORT).show()
                                }
                            ) {
                                Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Copier GABC")
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
            }

            // Live Preview of the entered Official GABC code
            item {
                Text(
                    text = "Aperçu de la partition grégorienne :",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                )
                GregorianScoreView(
                    rawGabc = customGabcText,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        }
    }
}
