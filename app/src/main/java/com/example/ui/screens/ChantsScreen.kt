package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoFixHigh
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Web
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.example.liturgy.data.JgabcDatabase
import com.example.liturgy.data.JgabcFeastSet
import com.example.liturgy.data.KyrialeLibrary
import com.example.liturgy.data.PropersLibrary
import com.example.liturgy.gabc.GregorianScoreView
import com.example.liturgy.gabc.PropersHtmlWebView
import com.example.ui.theme.RubricRed

@Composable
fun ChantsScreen(
    modifier: Modifier = Modifier
) {
    var selectedSection by remember { mutableIntStateOf(0) }
    val sections = listOf(
        "Propres de la Messe (jgabc)",
        "Kyriale & Ordinaire",
        "Éditeur GABC Officiel",
        "Outil Propers.html"
    )

    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current

    var allFeasts by remember { mutableStateOf<List<JgabcFeastSet>>(emptyList()) }
    var isLoadingFeasts by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        allFeasts = JgabcDatabase.getOrLoadAllFeasts(context)
        isLoadingFeasts = false
    }

    var selectedProperIndex by remember { mutableIntStateOf(0) }
    var searchQuery by remember { mutableStateOf("") }
    var selectedCycle by remember { mutableStateOf("Tous") }
    val cycles = listOf("Tous", "Temporale", "Sanctorale", "Communia", "Kyriale")

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

    val filteredFeasts = remember(allFeasts, searchQuery, selectedCycle) {
        allFeasts.filter { feast ->
            val matchCycle = selectedCycle == "Tous" || feast.seasonName.equals(selectedCycle, ignoreCase = true)
            val matchSearch = searchQuery.isBlank() ||
                feast.title.contains(searchQuery, ignoreCase = true) ||
                feast.subtitle.contains(searchQuery, ignoreCase = true) ||
                feast.key.contains(searchQuery, ignoreCase = true) ||
                feast.chants.any { it.title.contains(searchQuery, ignoreCase = true) || it.biblicalRef.contains(searchQuery, ignoreCase = true) }
            matchCycle && matchSearch
        }
    }

    if (selectedSection == 3) {
        // Propers.html webview full experience
        Column(modifier = modifier.fillMaxSize()) {
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
            PropersHtmlWebView(modifier = Modifier.weight(1f))
        }
        return
    }

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
                    text = "Graduale & Kyriale Romanum (jgabc / propers.html)",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "376 formulaires complets du Temporal et Sanctoral avec plus de 1 850 partitions GABC officielles, notations de Solesmes et textes bibliques.",
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
                // Search bar for all 376 feasts
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = {
                        searchQuery = it
                        selectedProperIndex = 0
                    },
                    placeholder = { Text("Rechercher une fête, messe ou chant...") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = RubricRed) },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            androidx.compose.material3.IconButton(onClick = { searchQuery = "" }) {
                                Icon(Icons.Default.Clear, contentDescription = "Effacer")
                            }
                        }
                    },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .testTag("feast_search_input")
                )

                // Cycle Filter Chips (Temporale, Sanctorale, Communia, Kyriale)
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    items(cycles) { cycle ->
                        FilterChip(
                            selected = selectedCycle == cycle,
                            onClick = {
                                selectedCycle = cycle
                                selectedProperIndex = 0
                            },
                            label = { Text(cycle) },
                            modifier = Modifier.testTag("cycle_chip_$cycle")
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                if (isLoadingFeasts) {
                    Box(modifier = Modifier.fillMaxWidth().padding(24.dp), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = RubricRed)
                    }
                } else if (filteredFeasts.isEmpty()) {
                    Text(
                        text = "Aucune fête trouvée pour cette recherche.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(12.dp)
                    )
                } else {
                    // Feasts selector chips
                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        itemsIndexed(filteredFeasts) { index, set ->
                            FilterChip(
                                selected = selectedProperIndex == index,
                                onClick = { selectedProperIndex = index },
                                label = { Text(set.title.split("(").first().trim()) },
                                modifier = Modifier.testTag("proper_chip_$index")
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(6.dp))
            }

            if (filteredFeasts.isNotEmpty()) {
                val currentFeastIndex = selectedProperIndex.coerceIn(0, filteredFeasts.lastIndex)
                val currentSet = filteredFeasts[currentFeastIndex]

                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(
                                text = currentSet.title,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            if (currentSet.subtitle.isNotBlank()) {
                                Text(
                                    text = currentSet.subtitle,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Text(
                                text = "Cycle : ${currentSet.seasonName} • ${currentSet.chants.size} partitions",
                                style = MaterialTheme.typography.labelSmall,
                                color = RubricRed
                            )
                        }
                    }
                }

                // 1. Collecta / Oraison du jour
                if (currentSet.collectLatin.isNotBlank()) {
                    item {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            shape = RoundedCornerShape(8.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            border = androidx.compose.foundation.BorderStroke(1.dp, RubricRed.copy(alpha = 0.25f))
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = "Collecta • Oraison du jour",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = RubricRed
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = currentSet.collectLatin,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                if (currentSet.collectVernacular.isNotBlank()) {
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = currentSet.collectVernacular,
                                        style = MaterialTheme.typography.bodySmall.copy(fontStyle = androidx.compose.ui.text.font.FontStyle.Italic),
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }

                // 2. Epistola / Épître du jour
                if (currentSet.epistleLatin.isNotBlank()) {
                    item {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            shape = RoundedCornerShape(8.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.25f))
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = "Epistola • ${currentSet.epistleRef.ifBlank { "Épître" }}",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = currentSet.epistleLatin,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                if (currentSet.epistleVernacular.isNotBlank()) {
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = currentSet.epistleVernacular,
                                        style = MaterialTheme.typography.bodySmall.copy(fontStyle = androidx.compose.ui.text.font.FontStyle.Italic),
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }

                // 3. Render all chants of the selected feast
                items(currentSet.chants) { chant ->
                    GregorianScoreView(
                        rawGabc = chant.gabc,
                        title = "${chant.part} - ${chant.title}",
                        mode = chant.mode,
                        translation = chant.biblicalRef,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }

                // 4. Evangelium / Saint Évangile du jour
                if (currentSet.gospelLatin.isNotBlank()) {
                    item {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            shape = RoundedCornerShape(8.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            border = androidx.compose.foundation.BorderStroke(1.dp, RubricRed.copy(alpha = 0.25f))
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = "Sanctum Evangelium • ${currentSet.gospelRef.ifBlank { "Évangile" }}",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = RubricRed
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = currentSet.gospelLatin,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                if (currentSet.gospelVernacular.isNotBlank()) {
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = currentSet.gospelVernacular,
                                        style = MaterialTheme.typography.bodySmall.copy(fontStyle = androidx.compose.ui.text.font.FontStyle.Italic),
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }

                // 5. Secreta & Postcommunio
                if (currentSet.secretLatin.isNotBlank() || currentSet.postcommunionLatin.isNotBlank()) {
                    item {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            shape = RoundedCornerShape(8.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.25f))
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                if (currentSet.secretLatin.isNotBlank()) {
                                    Text(
                                        text = "Secreta • Prière Secrète",
                                        style = MaterialTheme.typography.labelMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = RubricRed
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(text = currentSet.secretLatin, style = MaterialTheme.typography.bodySmall)
                                    Spacer(modifier = Modifier.height(8.dp))
                                }
                                if (currentSet.postcommunionLatin.isNotBlank()) {
                                    Text(
                                        text = "Postcommunio • Prière de Postcommunion",
                                        style = MaterialTheme.typography.labelMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = RubricRed
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(text = currentSet.postcommunionLatin, style = MaterialTheme.typography.bodySmall)
                                }
                            }
                        }
                    }
                }
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
