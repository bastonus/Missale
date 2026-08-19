package com.example.liturgy.data

import com.example.liturgy.model.KyrialeSetting
import com.example.liturgy.model.LiturgicalDay
import com.example.liturgy.model.LiturgicalItem
import com.example.liturgy.model.LiturgicalSeason
import com.example.liturgy.model.Speaker

/**
 * Ordo Missae 1960 (Missale Romanum 1960) with Propers (Propres de la Messe selon jgabc / propers_modern),
 * Bilingual Latin-Vernacular text, rubrics in crimson red, versicles/responses, and integrated Gregorian chant scores.
 */
object OrdoMissaeData {

    fun generateOrdoMissae(day: LiturgicalDay, kyriale: KyrialeSetting): List<LiturgicalItem> {
        val items = mutableListOf<LiturgicalItem>()
        val propers = PropersLibrary.getForDay(day.latinTitle, day.season)

        // 1. Asperges me / Vidi aquam (Sundays)
        if (day.isSunday) {
            items.add(LiturgicalItem.HeaderSection("Asperges me / Vidi aquam", "Rite de l'aspersion dominicale"))
            items.add(LiturgicalItem.Rubric("Sacerdos cum ministris ante altare genuflexus incipit antiphonam:"))
            if (day.season == LiturgicalSeason.EASTERTIDE) {
                items.add(
                    LiturgicalItem.ChantScore(
                        title = KyrialeLibrary.vidiAquam.title,
                        mode = KyrialeLibrary.vidiAquam.mode,
                        gabc = KyrialeLibrary.vidiAquam.gabc,
                        translation = KyrialeLibrary.vidiAquam.translation
                    )
                )
            } else {
                items.add(
                    LiturgicalItem.ChantScore(
                        title = KyrialeLibrary.aspergesMe.title,
                        mode = KyrialeLibrary.aspergesMe.mode,
                        gabc = KyrialeLibrary.aspergesMe.gabc,
                        translation = KyrialeLibrary.aspergesMe.translation
                    )
                )
            }
        }

        // 2. Prayers at the foot of the altar
        items.add(LiturgicalItem.HeaderSection("Preces ad gradus altaris", "Prières au bas de l'autel"))
        items.add(LiturgicalItem.Rubric("Sacerdos, facta altari debita reverentia, signat se signo crucis:"))
        items.add(
            LiturgicalItem.BilingualText(
                latin = "In nómine Patris, ✠ et Fílii, et Spíritus Sancti. Amen.",
                vernacular = "Au nom du Père, ✠ et du Fils, et du Saint-Esprit. Amen.",
                speaker = Speaker.CELEBRANT
            )
        )
        items.add(
            LiturgicalItem.VersicleResponse(
                versicleLatin = "Introíbo ad altáre Dei.",
                versicleVernacular = "J'avancerai jusqu'à l'autel de Dieu.",
                responseLatin = "Ad Deum qui laetíficat juventútem meam.",
                responseVernacular = "Vers Dieu qui réjouit ma jeunesse."
            )
        )

        // Judica me (omitted in Passiontide and Masses for the Dead)
        if (day.season != LiturgicalSeason.PASSIONTIDE && day.suggestedKyriale != KyrialeSetting.REQUIEM) {
            items.add(LiturgicalItem.Title("Psalmus 42", "Psaume 42", "Júdica me, Deus"))
            items.add(
                LiturgicalItem.BilingualText(
                    latin = "Júdica me, Deus, et discérne causam meam de gente non sancta : ab hómine iníquo et dolóso érue me.\n" +
                            "℟. Quia tu es, Deus, fortitúdo mea : quare me repulísti, et quare tristis incédo, dum afflígit me inimícus ?\n" +
                            "℣. Emítte lucem tuam et veritátem tuam : ipsa me deduxérunt, et adduxérunt in montem sanctum tuum, et in tabernácula tua.\n" +
                            "℟. Et introíbo ad altáre Dei : ad Deum qui laetíficat juventútem meam.\n" +
                            "℣. Confitébor tibi in cíthara, Deus, Deus meus : quare tristis es, ánima mea, et quare contúrbas me ?\n" +
                            "℟. Spera in Deo, quóniam adhuc confitébor illi : salutáre vultus mei, et Deus meus.\n" +
                            "℣. Glória Patri, et Fílio, et Spirítui Sancto.\n" +
                            "℟. Sicut erat in princípio, et nunc, et semper, et in saécula saeculórum. Amen.",
                    vernacular = "Rends-moi justice, ô Dieu, et défends ma cause contre une nation infidèle ; délivre-moi de l'homme perfide et pervers.\n" +
                            "℟. Car tu es, ô Dieu, ma force : pourquoi m'as-tu rejeté, et pourquoi marchai-je dans la tristesse pendant que l'ennemi m'opprime ?\n" +
                            "℣. Envoie ta lumière et ta vérité : qu'elles me guident et me conduisent à ta montagne sainte et à tes tabernacles.\n" +
                            "℟. Et j'irai à l'autel de Dieu, vers Dieu qui réjouit ma jeunesse.\n" +
                            "℣. Je te louerai sur la cithare, ô Dieu, mon Dieu : pourquoi es-tu triste, mon âme, et pourquoi me troubles-tu ?\n" +
                            "℟. Espère en Dieu, car je le louerai encore : Il est le salut de ma face et mon Dieu.\n" +
                            "℣. Gloire au Père, et au Fils, et au Saint-Esprit.\n" +
                            "℟. Comme il était au commencement, maintenant et toujours, pour les siècles des siècles. Amen.",
                    dropCap = true
                )
            )
        }

        // Confiteor
        items.add(LiturgicalItem.HeaderSection("Confiteor", "Confiteor du Célébrant et des Ministres"))
        items.add(LiturgicalItem.Rubric("Sacerdos, inclinatus profunde, dicit:"))
        items.add(
            LiturgicalItem.BilingualText(
                latin = "Confíteor Deo omnipoténti, beátae Maríae semper Vírgini, beáto Michaéli Archángelo, beáto Joánni Baptístae, sanctis Apóstolis Petro et Paulo, ómnibus Sanctis, et vobis fratres : quia peccávi nimis cogitatióne, verbo et ópere : mea culpa, mea culpa, mea máxima culpa. Ideo precor beátam Maríam semper Vírginem, beátum Michaélem Archángelum, beátum Joánnem Baptístam, sanctos Apóstolos Petrum et Paulum, omnes Sanctos, et vos fratres, oráre pro me ad Dóminum Deum nostrum.",
                vernacular = "Je confesse à Dieu tout-puissant, à la bienheureuse Marie toujours Vierge, à saint Michel Archange, à saint Jean-Baptiste, aux saints Apôtres Pierre et Paul, à tous les Saints et à vous, mes frères, que j'ai beaucoup péché en pensée, en parole et par action : c'est ma faute, c'est ma faute, c'est ma très grande faute. C'est pourquoi je supplie la bienheureuse Marie toujours Vierge, saint Michel Archange, saint Jean-Baptiste, les saints Apôtres Pierre et Paul, tous les Saints et vous, mes frères, de prier pour moi le Seigneur notre Dieu.",
                speaker = Speaker.CELEBRANT
            )
        )
        items.add(
            LiturgicalItem.BilingualText(
                latin = "Misereátur tui omnípotens Deus, et dimíssis peccátis tuis, perdúcat te ad vitam aetérnam.",
                vernacular = "Que le Dieu tout-puissant vous fasse miséricorde, vous pardonne vos péchés et vous conduise à la vie éternelle.",
                speaker = Speaker.SERVER
            )
        )
        items.add(
            LiturgicalItem.BilingualText(
                latin = "Amen.",
                vernacular = "Amen.",
                speaker = Speaker.CELEBRANT
            )
        )
        items.add(LiturgicalItem.Rubric("Ministri repetunt Confiteor, dicentes 'tibi, pater' et 'te, pater':"))
        items.add(
            LiturgicalItem.BilingualText(
                latin = "Misereátur vestri omnípotens Deus, et dimíssis peccátis vestris, perdúcat vos ad vitam aetérnam. Amen.\nIndulgéntiam, ✠ absolutiónem et remissiónem peccatórum nostrórum tríbuat nobis omnípotens et miséricors Dóminus. Amen.",
                vernacular = "Que le Dieu tout-puissant vous fasse miséricorde, vous pardonne vos péchés et vous conduise à la vie éternelle. Amen.\nQue le Seigneur tout-puissant et miséricordieux nous accorde l'indulgence, ✠ l'absolution et la rémission de nos péchés. Amen.",
                speaker = Speaker.CELEBRANT
            )
        )

        // 3. Propre : Introitus
        items.add(LiturgicalItem.HeaderSection("Introitus (Proprium Missae)", "Introït (Propre de la Messe)"))
        if (propers != null) {
            items.add(propers.introit.toLiturgicalItem())
        }

        // 4. Kyriale
        items.add(LiturgicalItem.HeaderSection("Kyrie eleison", "Kyrie (Ordinaire de la Messe)"))
        items.add(LiturgicalItem.Rubric("Kyriale: " + kyriale.title))

        val kyrieChant = when (kyriale) {
            KyrialeSetting.KYRIALE_I -> KyrialeLibrary.kyrieLuxEtOrigo
            KyrialeSetting.KYRIALE_IX -> KyrialeLibrary.kyrieCumJubilo
            KyrialeSetting.KYRIALE_XI -> KyrialeLibrary.kyrieOrbisFactor
            else -> KyrialeLibrary.kyrieDeAngelis
        }

        items.add(
            LiturgicalItem.ChantScore(
                title = kyrieChant.title,
                mode = kyrieChant.mode,
                gabc = kyrieChant.gabc,
                translation = kyrieChant.translation
            )
        )

        // 5. Gloria in excelsis (omitted in Advent/Lent and Requiem)
        if (day.season != LiturgicalSeason.ADVENT && day.season != LiturgicalSeason.LENT &&
            day.season != LiturgicalSeason.SEPTUAGESIMA && kyriale != KyrialeSetting.REQUIEM
        ) {
            items.add(LiturgicalItem.HeaderSection("Gloria in excelsis Deo", "Gloire à Dieu au plus haut des cieux"))
            items.add(
                LiturgicalItem.BilingualText(
                    latin = "Glória in excélsis Deo, et in terra pax homínibus bonae voluntátis.\n" +
                            "Laudámus te, benedícimus te, adorámus te, glorificámus te.\n" +
                            "Grátias ágimus tibi propter magnam glóriam tuam :\n" +
                            "Dómine Deus, Rex caeléstis, Deus Pater omnípotens.\n" +
                            "Dómine Fili unigénite, Jesu Christe ;\n" +
                            "Dómine Deus, Agnus Dei, Fílius Patris :\n" +
                            "Qui tollis peccáta mundi, miserére nobis ;\n" +
                            "Qui tollis peccáta mundi, súscipe deprecatiónem nostram ;\n" +
                            "Qui sedes ad déxteram Patris, miserére nobis.\n" +
                            "Quóniam tu solus Sanctus, tu solus Dóminus, tu solus Altíssimus, Jesu Christe, cum Sancto Spíritu : in glória Dei Patris. Amen.",
                    vernacular = "Gloire à Dieu au plus haut des cieux, et paix sur la terre aux hommes de bonne volonté.\n" +
                            "Nous te louons, nous te bénissons, nous t'adorons, nous te glorifions.\n" +
                            "Nous te rendons grâces pour ton immense gloire :\n" +
                            "Seigneur Dieu, Roi du ciel, Dieu le Père tout-puissant.\n" +
                            "Seigneur, Fils unique, Jésus-Christ ;\n" +
                            "Seigneur Dieu, Agneau de Dieu, le Fils du Père :\n" +
                            "Toi qui enlèves les péchés du monde, prends pitié de nous ;\n" +
                            "Toi qui enlèves les péchés du monde, reçois notre prière ;\n" +
                            "Toi qui es assis à la droite du Père, prends pitié de nous.\n" +
                            "Car toi seul es Saint, toi seul es Seigneur, toi seul es le Très-Haut, Jésus-Christ, avec le Saint-Esprit, dans la gloire de Dieu le Père. Amen.",
                    dropCap = true
                )
            )
        }

        // 6. Collecta, Epistola
        items.add(LiturgicalItem.HeaderSection("Liturgia Verbi - Collecta (Oratio)", "Liturgie de la Parole - Oraison du Jour"))
        items.add(
            LiturgicalItem.VersicleResponse(
                versicleLatin = "Dóminus vobíscum.",
                versicleVernacular = "Le Seigneur soit avec vous.",
                responseLatin = "Et cum spíritu tuo.",
                responseVernacular = "Et avec votre esprit."
            )
        )
        
        val collectLat = propers?.collectLatin?.ifBlank { null }
            ?: "Orémus.\nDeus, qui hodiérna die per Unigénitum tuum, aeternitátis nobis áditum devícta morte reserásti : vota nostra, quae praeveniéndo aspíras, étiam adjuvándo proséquere. Per eúmdem Dóminum nostrum Jesum Christum Fílium tuum, qui tecum vivit et regnat in unitáte Spíritus Sancti Deus, per ómnia saécula saeculórum. Amen."
        val collectVern = propers?.collectVernacular?.ifBlank { null }
            ?: "Prions.\nÔ Dieu, qui en ce jour, par votre Fils unique vainqueur de la mort, nous avez ouvert l'accès de l'éternité, secondez par votre grâce les désirs que vous nous inspirez. Par le même Jésus-Christ, votre Fils, notre Seigneur, qui vit et règne avec vous en l'unité du Saint-Esprit, Dieu, pour tous les siècles des siècles. Amen."
        items.add(
            LiturgicalItem.BilingualText(
                latin = collectLat,
                vernacular = collectVern,
                speaker = Speaker.CELEBRANT,
                dropCap = true
            )
        )

        // Epistola (Reading of the day)
        if (propers != null && propers.epistleLatin.isNotBlank()) {
            val epRef = if (propers.epistleRef.isNotBlank()) " (${propers.epistleRef})" else ""
            items.add(LiturgicalItem.HeaderSection("Epistola$epRef", "Épître du jour$epRef"))
            items.add(LiturgicalItem.Rubric("Lector vel Subdiaconus cantat Epistolam:"))
            items.add(
                LiturgicalItem.BilingualText(
                    latin = propers.epistleLatin,
                    vernacular = propers.epistleVernacular.ifBlank { propers.epistleLatin },
                    speaker = Speaker.LECTOR,
                    dropCap = true
                )
            )
            items.add(
                LiturgicalItem.VersicleResponse(
                    versicleLatin = "Deo grátias.",
                    versicleVernacular = "Rendons grâces à Dieu.",
                    responseLatin = "",
                    responseVernacular = ""
                )
            )
        }

        // 7. Propre : Graduale & Alleluia / Tractus / Sequentia
        items.add(LiturgicalItem.HeaderSection("Graduale & Alleluia (Proprium Missae)", "Graduel & Alléluia / Trait / Séquence (Propre)"))
        if (propers != null) {
            items.add(propers.gradual.toLiturgicalItem())
            items.add(propers.alleluiaOrTract.toLiturgicalItem())
            if (propers.sequence != null) {
                items.add(propers.sequence.toLiturgicalItem())
            }
        }

        // Evangelium (Gospel of the day)
        if (propers != null && propers.gospelLatin.isNotBlank()) {
            val gospRef = if (propers.gospelRef.isNotBlank()) " (${propers.gospelRef})" else ""
            items.add(LiturgicalItem.HeaderSection("Sanctum Evangelium$gospRef", "Saint Évangile du jour$gospRef"))
            items.add(
                LiturgicalItem.VersicleResponse(
                    versicleLatin = "Dóminus vobíscum.",
                    versicleVernacular = "Le Seigneur soit avec vous.",
                    responseLatin = "Et cum spíritu tuo.",
                    responseVernacular = "Et avec votre esprit."
                )
            )
            items.add(
                LiturgicalItem.VersicleResponse(
                    versicleLatin = "Sequéntia ✠ sancti Evangélii secúndum ${propers.gospelRef.substringBefore(':').trim()}.",
                    versicleVernacular = "Suite du saint Évangile selon ${propers.gospelRef.substringBefore(':').trim()}.",
                    responseLatin = "Glória tibi, Dómine.",
                    responseVernacular = "Gloire à toi, Seigneur."
                )
            )
            items.add(
                LiturgicalItem.BilingualText(
                    latin = propers.gospelLatin,
                    vernacular = propers.gospelVernacular.ifBlank { propers.gospelLatin },
                    speaker = Speaker.PRIEST,
                    dropCap = true
                )
            )
            items.add(
                LiturgicalItem.VersicleResponse(
                    versicleLatin = "Laus tibi, Christe.",
                    versicleVernacular = "Louange à toi, ô Christ.",
                    responseLatin = "",
                    responseVernacular = ""
                )
            )
        }

        // 8. Credo
        items.add(LiturgicalItem.HeaderSection("Credo", "Symbole des Apôtres de Nicée-Constantinople"))
        items.add(
            LiturgicalItem.ChantScore(
                title = KyrialeLibrary.credoIII.title,
                mode = KyrialeLibrary.credoIII.mode,
                gabc = KyrialeLibrary.credoIII.gabc,
                translation = KyrialeLibrary.credoIII.translation
            )
        )

        // 9. Propre : Offertorium
        items.add(LiturgicalItem.HeaderSection("Offertorium (Proprium Missae)", "Offertoire (Chant du Propre)"))
        if (propers != null) {
            items.add(propers.offertory.toLiturgicalItem())
        }

        items.add(LiturgicalItem.Rubric("Sacerdos offert hostiam dicens:"))
        items.add(
            LiturgicalItem.BilingualText(
                latin = "Súscipe, sancte Pater, omnípotens aetérne Deus, hanc immaculátam hóstiam, quam ego indígnus fámulus tuus óffero tibi Deo meo vivo et vero, pro innumerabílibus peccátis, et offensiónibus, et negligéntiis meis, et pro ómnibus circumstántibus, sed et pro ómnibus fidélibus christiánis vivis atque defúnctis : ut mihi, et illis profíciat ad salútem in vitam aetérnam. Amen.",
                vernacular = "Reçois, Père saint, Dieu tout-puissant et éternel, cette hostie immaculée que moi, ton indigne serviteur, je t'offre à toi, mon Dieu vivant et vrai, pour mes péchés, offenses et négligences sans nombre, pour tous ceux qui m'entourent, et pour tous les fidèles chrétiens vivants et défunts : afin qu'elle serve à mon salut et au leur pour la vie éternelle. Amen.",
                speaker = Speaker.CELEBRANT,
                dropCap = true
            )
        )

        // Secreta (Secret prayer of the day)
        if (propers != null && propers.secretLatin.isNotBlank()) {
            items.add(LiturgicalItem.HeaderSection("Secreta (Oratio super oblata)", "Prière Secrète du jour"))
            items.add(
                LiturgicalItem.BilingualText(
                    latin = propers.secretLatin,
                    vernacular = propers.secretVernacular.ifBlank { propers.secretLatin },
                    speaker = Speaker.CELEBRANT,
                    dropCap = true
                )
            )
        }

        // 10. Canon Missae
        items.add(LiturgicalItem.HeaderSection("Canon Missae", "Canon de la Messe"))
        items.add(LiturgicalItem.Rubric("Sacerdos, extensis et elevatis manibus, iunctis et capite inclinato, incipit:"))
        items.add(
            LiturgicalItem.BilingualText(
                latin = "Te ígitur, clementíssime Pater, per Jesum Christum Fílium tuum, Dóminum nostrum, súpplices rogámus, ac pétimus, uti accépta hábeas et benedícas, haec ✠ dona, haec ✠ múnera, haec ✠ sancta sacrifícia illibáta, in primis, quae tibi offérimus pro Ecclésia tua sancta cathólica : quam pacificáre, custodíre, adunáre et régere dignéris toto orbe terrárum : una cum fámulo tuo Papa nostro N., et Antístite nostro N., et ómnibus orthodóxis, atque cathólicae et apostólicae fídei cultóribus.",
                vernacular = "Nous te supplions donc avec confiance, Père très clément, par Jésus-Christ ton Fils, notre Seigneur, et nous te demandons d'agréer et de bénir ces ✠ dons, ces ✠ présents, ces ✠ saints sacrifices sans tache, que nous t'offrons en premier lieu pour ta sainte Église catholique : daigne lui donner la paix, la protéger, la rassembler dans l'unité et la gouverner par toute la terre, en union avec ton serviteur notre Pape N., notre Évêque N., et tous les fidèles gardiens de la foi catholique et apostolique.",
                speaker = Speaker.CELEBRANT,
                dropCap = true
            )
        )

        // Consecratio
        items.add(LiturgicalItem.HeaderSection("Consecratio", "Consécration eucharistique"))
        items.add(LiturgicalItem.Rubric("Consecratio Hostiae:"))
        items.add(
            LiturgicalItem.BilingualText(
                latin = "HOC EST ENIM CORPUS MEUM.",
                vernacular = "CAR CECI EST MON CORPS.",
                speaker = Speaker.CELEBRANT
            )
        )
        items.add(LiturgicalItem.Rubric("Consecratio Calicis:"))
        items.add(
            LiturgicalItem.BilingualText(
                latin = "HIC EST ENIM CALIX SÁNGUINIS MEI, NOVI ET AETÉRNI TESTAMÉNTI : MYSTÉRIUM FÍDEI : QUI PRO VOBIS ET PRO MULTIS EFFUNDÉTUR IN REMISSIÓNEM PECCATÓRUM.",
                vernacular = "CAR CECI EST LE CALICE DE MON SANG, DU NOUVEAU ET ÉTERNEL TESTAMENT : LE MYSTÈRE DE LA FOI : QUI SERA VERSÉ POUR VOUS ET POUR BEAUCOUP EN RÉMISSION DES PÉCHÉS.",
                speaker = Speaker.CELEBRANT
            )
        )

        // 11. Sanctus & Agnus Dei
        items.add(LiturgicalItem.HeaderSection("Sanctus & Agnus Dei", "Sanctus & Agneau de Dieu"))
        val sanctusChant = KyrialeLibrary.sanctusDeAngelis
        items.add(
            LiturgicalItem.ChantScore(
                title = sanctusChant.title,
                mode = sanctusChant.mode,
                gabc = sanctusChant.gabc,
                translation = sanctusChant.translation
            )
        )

        val agnusChant = KyrialeLibrary.agnusDeAngelis
        items.add(
            LiturgicalItem.ChantScore(
                title = agnusChant.title,
                mode = agnusChant.mode,
                gabc = agnusChant.gabc,
                translation = agnusChant.translation
            )
        )

        // 12. Propre : Communio
        items.add(LiturgicalItem.HeaderSection("Communio (Proprium Missae)", "Communion (Chant du Propre)"))
        if (propers != null) {
            items.add(propers.communion.toLiturgicalItem())
        }

        // Postcommunio (Postcommunion prayer of the day)
        if (propers != null && propers.postcommunionLatin.isNotBlank()) {
            items.add(LiturgicalItem.HeaderSection("Postcommunio (Oratio post communionem)", "Prière de Postcommunion du jour"))
            items.add(
                LiturgicalItem.VersicleResponse(
                    versicleLatin = "Dóminus vobíscum.",
                    versicleVernacular = "Le Seigneur soit avec vous.",
                    responseLatin = "Et cum spíritu tuo.",
                    responseVernacular = "Et avec votre esprit."
                )
            )
            items.add(
                LiturgicalItem.BilingualText(
                    latin = "Orémus.\n" + propers.postcommunionLatin,
                    vernacular = "Prions.\n" + propers.postcommunionVernacular.ifBlank { propers.postcommunionLatin },
                    speaker = Speaker.CELEBRANT,
                    dropCap = true
                )
            )
        }

        // 13. Ultimum Evangelium (St John 1:1-14)
        items.add(LiturgicalItem.HeaderSection("Ultimum Evangelium (Joann 1, 1-14)", "Dernier Évangile selon saint Jean"))
        items.add(
            LiturgicalItem.BilingualText(
                latin = "In princípio erat Verbum, et Verbum erat apud Deum, et Deus erat Verbum. Hoc erat in princípio apud Deum. Omnia per ipsum facta sunt : et sine ipso factum est nihil, quod factum est : in ipso vita erat, et vita erat lux hóminum : et lux in ténebris lucet, et ténebrae eam non comprehendérunt.\n" +
                        "Fuit homo missus a Deo, cui nomen erat Joánnes. Hic venit in testimónium, ut testimónium perhibéret de lúmine, ut omnes créderent per illum. Non erat ille lux, sed ut testimónium perhibéret de lúmine.\n" +
                        "Erat lux vera, quae illúminat omnem hóminem veniéntem in hunc mundum. In mundo erat, et mundus per ipsum factus est, et mundus eum non cognóvit. In própria venit, et sui eum non recepérunt. Quotquot autem recepérunt eum, dedit eis potestátem fílios Dei fíeri, his, qui credunt in nómine ejus : qui non ex sanguínibus, neque ex voluntáte carnis, neque ex voluntáte viri, sed ex Deo nati sunt. (Hic genuflectitur) ET VERBUM CARO FACTUM EST, et habitávit in nobis : et vídimus glóriam ejus, glóriam quasi Unigéniti a Patre, plenum grátiae et veritátis.",
                vernacular = "Au commencement était le Verbe, et le Verbe était auprès de Dieu, et le Verbe était Dieu. Il était au commencement auprès de Dieu. Tout a été fait par lui, et rien de ce qui a été fait n'a été fait sans lui. En lui était la vie, et la vie était la lumière des hommes ; et la lumière brille dans les ténèbres, et les ténèbres ne l'ont point reçue.\n" +
                        "Il y eut un homme envoyé de Dieu, dont le nom était Jean. Il vint comme témoin, pour rendre témoignage à la lumière, afin que tous crussent par lui. Il n'était pas la lumière, mais il devait rendre témoignage à la lumière.\n" +
                        "C'était la vraie lumière qui illumine tout homme venant en ce monde. Il était dans le monde, et le monde a été fait par lui, et le monde ne l'a pas connu. Il est venu chez les siens, et les siens ne l'ont point reçu. Mais à tous ceux qui l'ont reçu, il a donné le pouvoir de devenir enfants de Dieu, à ceux qui croient en son nom, qui ne sont pas nés du sang, ni de la volonté de la chair, ni de la volonté de l'homme, mais de Dieu. (Ici on fait la génuflexion) ET LE VERBE S'EST FAIT CHAIR, et il a habité parmi nous ; et nous avons vu sa gloire, gloire comme celle du Fils unique venu du Père, plein de grâce et de vérité.",
                dropCap = true
            )
        )

        return items
    }
}
