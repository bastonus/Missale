package com.example.liturgy.data

import com.example.liturgy.model.LiturgicalDay
import com.example.liturgy.model.LiturgicalItem
import com.example.liturgy.model.LiturgicalSeason
import com.example.liturgy.model.MarianAntiphon
import com.example.liturgy.model.Speaker
import java.time.DayOfWeek

/**
 * Liturgical Office of Compline (Completorium) according to 1960 Roman Breviary Rubrics.
 */
object ComplineData {

    fun generateCompline(day: LiturgicalDay): List<LiturgicalItem> {
        val items = mutableListOf<LiturgicalItem>()

        // 1. Initial Blessing (Benedictio)
        items.add(LiturgicalItem.HeaderSection("Initium Completorii", "Début des Complies"))
        items.add(LiturgicalItem.Rubric("Lector incipit:"))
        items.add(
            LiturgicalItem.BilingualText(
                latin = "Jube, domne, benedícere.",
                vernacular = "Veuillez, mon Père, me bénir.",
                speaker = Speaker.LECTOR
            )
        )
        items.add(LiturgicalItem.Rubric("Benedictio:"))
        items.add(
            LiturgicalItem.BilingualText(
                latin = "Noctem quiétam et finem perféctum concédat nobis Dóminus omnípotens.",
                vernacular = "Que le Seigneur tout-puissant nous accorde une nuit tranquille et une fin parfaite.",
                speaker = Speaker.PRIEST
            )
        )
        items.add(
            LiturgicalItem.BilingualText(
                latin = "Amen.",
                vernacular = "Amen.",
                speaker = Speaker.SERVER
            )
        )

        // 2. Short Lesson (Lectio brevis - 1 Pet 5:8-9)
        items.add(LiturgicalItem.HeaderSection("Lectio Brevis (1 Petr 5, 8-9)", "Petite Lecture"))
        items.add(
            LiturgicalItem.BilingualText(
                latin = "Fratres : Sóbrii estóte, et vigiláte : quia adversárius vester diábolus tamquam leo rúgiens círcuit, quaerens quem dévoret : cui resístite fortes in fide. Tu autem, Dómine, miserére nobis.",
                vernacular = "Mes frères : Soyez sobres et veillez ; car votre adversaire, le diable, rôde comme un lion rugissant, cherchant qui dévorer : résistez-lui, fermes dans la foi. Et vous, Seigneur, ayez pitié de nous.",
                speaker = Speaker.LECTOR,
                dropCap = true
            )
        )
        items.add(
            LiturgicalItem.BilingualText(
                latin = "Deo grátias.",
                vernacular = "Rendons grâces à Dieu.",
                speaker = Speaker.SERVER
            )
        )

        // 3. Confiteor & Absolutio
        items.add(LiturgicalItem.HeaderSection("Confessio & Absolutio", "Confession des péchés"))
        items.add(
            LiturgicalItem.VersicleResponse(
                versicleLatin = "Adjutórium nostrum in nómine Dómini.",
                versicleVernacular = "Notre secours est dans le nom du Seigneur.",
                responseLatin = "Qui fecit caelum et terram.",
                responseVernacular = "Qui a fait le ciel et la terre."
            )
        )
        items.add(LiturgicalItem.Rubric("Fit examinatio conscientiae secreto, deinde dicitur Confiteor:"))
        items.add(
            LiturgicalItem.BilingualText(
                latin = "Confíteor Deo omnipoténti, beátae Maríae semper Vírgini, beáto Michaéli Archángelo, beáto Joánni Baptístae, sanctis Apóstolis Petro et Paulo, et ómnibus Sanctis, quia peccávi nimis cogitatióne, verbo et ópere : mea culpa, mea culpa, mea máxima culpa. Ideo precor beátam Maríam semper Vírginem, beátum Michaélem Archángelum, beátum Joánnem Baptístam, sanctos Apóstolos Petrum et Paulum, et omnes Sanctos, oráre pro me ad Dóminum Deum nostrum.",
                vernacular = "Je confesse à Dieu tout-puissant, à la bienheureuse Marie toujours Vierge, à saint Michel Archange, à saint Jean-Baptiste, aux saints Apôtres Pierre et Paul, et à tous les Saints, que j'ai beaucoup péché en pensée, en parole et par action : c'est ma faute, c'est ma faute, c'est ma très grande faute. C'est pourquoi je supplie la bienheureuse Marie toujours Vierge, saint Michel Archange, saint Jean-Baptiste, les saints Apôtres Pierre et Paul, et tous les Saints, de prier pour moi le Seigneur notre Dieu.",
                speaker = Speaker.ALL
            )
        )
        items.add(
            LiturgicalItem.BilingualText(
                latin = "Misereátur nostri omnípotens Deus, et dimíssis peccátis nostris, perdúcat nos ad vitam aetérnam. Amen.\nIndulgéntiam, absolutiónem et remissiónem peccatórum nostrórum tríbuat nobis omnípotens et miséricors Dóminus. Amen.",
                vernacular = "Que le Dieu tout-puissant nous fasse miséricorde, qu'Il nous pardonne nos péchés et nous conduise à la vie éternelle. Amen.\nQue le Seigneur tout-puissant et miséricordieux nous accorde le pardon, l'absolution et la rémission de nos péchés. Amen.",
                speaker = Speaker.PRIEST
            )
        )

        // 4. Versicles before Psalms
        items.add(
            LiturgicalItem.VersicleResponse(
                versicleLatin = "Convérte nos, Deus salutáris noster.",
                versicleVernacular = "Convertis-nous, Dieu notre Sauveur.",
                responseLatin = "Et avérte iram tuam a nobis.",
                responseVernacular = "Et détourne ta colère de nous."
            )
        )
        items.add(
            LiturgicalItem.VersicleResponse(
                versicleLatin = "Deus ✠ in adjutórium meum inténde.",
                versicleVernacular = "Dieu ✠, viens à mon aide.",
                responseLatin = "Dómine, ad adjuvándum me festína.\nGlória Patri, et Fílio, et Spirítui Sancto : Sicut erat in princípio, et nunc, et semper, et in saécula saeculórum. Amen. " + (if (day.season == LiturgicalSeason.LENT || day.season == LiturgicalSeason.PASSIONTIDE || day.season == LiturgicalSeason.SEPTUAGESIMA) "Laus tibi Dómine, Rex aetérnae glóriae." else "Allelúia."),
                responseVernacular = "Seigneur, hâte-toi de me secourir.\nGloire au Père, au Fils et au Saint-Esprit, comme il était au commencement, maintenant et toujours, pour les siècles des siècles. Amen."
            )
        )

        // 5. Psalms according to day of week
        items.addAll(getComplinePsalms(day))

        // 6. Hymnus: Te lucis ante terminum
        items.add(LiturgicalItem.HeaderSection("Hymnus", "Hymne"))
        items.add(
            LiturgicalItem.ChantScore(
                title = "Te lucis ante terminum (Ton ordinaire)",
                mode = "VIII",
                gabc = """(c4) TE(g) lu(h)cis(j) an(j)te(j) tér(j)mi(h)num,(i.) (;) Re(h)rum(g) Cre(h)á(j)tor,(i) pós(h)ci(g)mus,(h.) (:) Ut(h) pro(j) tu(i)a(h) cle(g)mén(h)ti(g)a,(f.) (;) Sis(h) praé(j)sul(i) et(h) cus(g)tó(h)di(g)a.(g.) (::)
2. Pro(g)cul(h) re(j)cé(j)dant(j) sóm(j)ni(h)a,(i.) (;) Et(h) nóc(g)ti(h)um(j) phan(i)tás(h)ma(g)ta ;(h.) (:) Hos(h)tém(j)que(i) nos(h)trum(g) cóm(h)pri(g)me,(f.) (;) Ne(h) pol(j)lu(i)án(h)tur(g) cór(h)po(g)ra.(g.) (::)
3. Praes(g)ta,(h) Pa(j)ter(j) pi(j)ís(j)si(h)me,(i.) (;) Pa(h)trí(g)que(h) com(j)par(i) U(h)ni(g)ce,(h.) (:) Cum(h) Spí(j)ri(i)tu(h) Pa(g)rá(h)cli(g)to(f.) (;) Re(h)gnans(j) per(i) om(h)ne(g) saé(h)cu(g)lum.(g.) (::) A(ghg)men.(fg..) (::)""",
                translation = "Avant que la lumière ne s'éteigne, nous te supplions, Créateur de toutes choses, d'être, par ta clémence habituelle, notre gardien et notre protecteur."
            )
        )

        // 7. Capitulum & Short Responsory
        items.add(LiturgicalItem.HeaderSection("Capitulum (Jer 14, 9)", "Capitule"))
        items.add(
            LiturgicalItem.BilingualText(
                latin = "Tu autem in nobis es, Dómine, et nomen sanctum tuum invocátum est super nos : ne derelínquas nos, Dómine Deus noster.",
                vernacular = "Mais toi, Seigneur, tu es au milieu de nous, et ton saint nom a été invoqué sur nous : ne nous abandonne pas, Seigneur notre Dieu.",
                speaker = Speaker.LECTOR
            )
        )
        items.add(
            LiturgicalItem.BilingualText(
                latin = "Deo grátias.",
                vernacular = "Rendons grâces à Dieu.",
                speaker = Speaker.SERVER
            )
        )

        items.add(LiturgicalItem.HeaderSection("Responsorium Breve", "Répons bref"))
        items.add(
            LiturgicalItem.ChantScore(
                title = "In manus tuas, Domine",
                mode = "VI",
                gabc = """(c4) IN(f) ma(f)nus(f) tu(g)as,(f) Dó(gh)mi(h)ne,(h.) *(;) Com(h)mén(ixg!hi)do(h) spí(g)ri(f)tum(gh) me(g)um.(f.) (::)
<sp>V/</sp>. Red(h)e(h)mís(ixhi)ti(h) nos,(h) Dó(gh)mi(g)ne,(g.) (;) De(g)us(g) ve(h)ri(g)tá(f)tis.(gh..) *(;) Com(h)mén(ixg!hi)do.(h) (::)
Gló(h)ri(h)a(h) Pa(ixhi)tri,(h) et(h) Fí(gh)li(g)o,(g.) (;) et(g) Spi(g)rí(h)tu(g)i(f) Sanc(gh)to.(h.) *(;) In(f) ma(f)nus.(f) (::)""",
                translation = "En tes mains, Seigneur, je remets mon esprit. Tu nous as rachetés, Seigneur, Dieu de vérité."
            )
        )

        items.add(
            LiturgicalItem.VersicleResponse(
                versicleLatin = "Custódi nos, Dómine, ut pupíllam óculi.",
                versicleVernacular = "Garde-nous, Seigneur, comme la prunelle de l'œil.",
                responseLatin = "Sub umbra alárum tuárum prótege nos.",
                responseVernacular = "Protège-nous à l'ombre de tes ailes."
            )
        )

        // 8. Canticum Simeonis: Nunc Dimittis
        items.add(LiturgicalItem.HeaderSection("Canticum Simeonis (Lc 2, 29-32)", "Cantique de Siméon"))
        items.add(
            LiturgicalItem.ChantScore(
                title = "Antiphona: Salva nos & Nunc dimittis",
                mode = "III",
                gabc = """(c4) SAl(g)va(hj) nos,(j) * Dó(jk)mi(j)ne,(j) vi(h)gi(j)lán(ih)tes,(g.) (;) cus(g)tó(hj)di(j) nos(j) dor(i)mi(h)én(ij)tes ;(i.) (:) ut(g) vi(i)gi(j)lé(k)mus(j) cum(h) Chris(j)to,(i.) (;) et(g) re(h)qui(j)es(i)cá(g)mus(h) in(h) pa(g.)ce.(g.) (::)
1. Nunc(g) di(hj)mít(j)tis(j) ser(j)vum(j) tu(j)um,(j) Dó(k)mi(j)ne,(j.) *(:) se(j)cún(j)dum(j) ver(j)bum(j) tu(ji)um(h) in(ij) pa(i)ce.(g.) (::)
2. Qui(g)a(hj) vi(j)dé(j)runt(j) ó(j)cu(j)li(j) me(k)i(j.) *(:) sa(j)lu(j)tá(ji)re(h) tu(ij)um.(i) (::)
3. Quod(g) pa(hj)rás(k)ti(j.) *(:) an(j)te(j) fá(j)ci(j)em(j) óm(j)ni(j)um(j) po(ji)pu(h)ló(ij)rum.(i) (::)
4. Lu(g)men(hj) ad(j) re(j)ve(j)la(j)ti(j)ó(j)nem(j) Gén(k)ti(j)um,(j.) *(:) et(j) gló(j)ri(j)am(j) ple(ji)bis(h) tu(ij)ae(i) Is(g)ra(h)el.(g.) (::)""",
                translation = "Sauve-nous, Seigneur, quand nous veillons, garde-nous quand nous dormons : afin que nous veillions avec le Christ et reposions dans la paix.\nMaintenant, Seigneur, tu laisses ton serviteur s'en aller en paix selon ta parole, car mes yeux ont vu ton salut."
            )
        )

        // 9. Oratio
        items.add(LiturgicalItem.HeaderSection("Oratio", "Oraison"))
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
                latin = "Orémus.\nVísi(g)ta,(h) quaé(h)su(h)mus,(h) Dó(h)mi(h)ne,(h) ha(h)bi(h)ta(h)ti(h)ó(h)nem(h) is(h)tam,(h) et(h) om(h)nes(h) in(h)sí(h)di(h)as(h) in(h)i(h)mí(h)ci(h) ab(h) e(h)a(h) lon(h)ge(h) re(h)pél(h)le :(h) An(h)ge(h)li(h) tu(h)i(h) sanc(h)ti(h) há(h)bi(h)tent(h) in(h) e(h)a,(h) qui(h) nos(h) in(h) pa(h)ce(h) cus(h)tó(h)di(h)ant ;(h) et(h) be(h)ne(h)díc(h)ti(h)o(h) tu(h)a(h) sit(h) su(h)per(h) nos(h) sem(h)per.\nPer Dóminum nostrum Jesum Christum, Fílium tuum : qui tecum vivit et regnat in unitáte Spíritus Sancti Deus, per ómnia saécula saeculórum. Amen.",
                vernacular = "Prions.\nVisite, nous t'en prions, Seigneur, cette demeure, et repousse loin d'elle toutes les embûches de l'ennemi ; que tes saints Anges y habitent pour nous garder dans la paix, et que ta bénédiction soit toujours sur nous.\nPar notre Seigneur Jésus-Christ, ton Fils, qui vit et règne avec toi dans l'unité du Saint-Esprit, Dieu, pour tous les siècles des siècles. Amen.",
                speaker = Speaker.PRIEST
            )
        )

        // 10. Concluding Blessing
        items.add(
            LiturgicalItem.VersicleResponse(
                versicleLatin = "Benedicámus Dómino.",
                versicleVernacular = "Bénissons le Seigneur.",
                responseLatin = "Deo grátias.",
                responseVernacular = "Rendons grâces à Dieu."
            )
        )
        items.add(
            LiturgicalItem.BilingualText(
                latin = "Benedícat et custódiat nos omnípotens et miséricors Dóminus, ✠ Pater, et Fílius, et Spíritus Sanctus. Amen.",
                vernacular = "Que le Seigneur tout-puissant et miséricordieux nous bénisse et nous garde, ✠ le Père, et le Fils, et le Saint-Esprit. Amen.",
                speaker = Speaker.PRIEST
            )
        )

        // 11. Final Marian Antiphon
        items.addAll(getMarianAntiphonItems(day.marianAntiphon))

        return items
    }

    private fun getComplinePsalms(day: LiturgicalDay): List<LiturgicalItem> {
        val list = mutableListOf<LiturgicalItem>()
        val dayOfWeek = day.date.dayOfWeek

        val antiphonLatin = if (day.season == LiturgicalSeason.EASTERTIDE) "Allelúia, allelúia, allelúia." else "Miserére mihi, Dómine, et exáudi oratiónem meam."
        val antiphonVernacular = if (day.season == LiturgicalSeason.EASTERTIDE) "Alléluia, alléluia, alléluia." else "Aie pitié de moi, Seigneur, et exauce ma prière."

        list.add(LiturgicalItem.HeaderSection("Psalmodia", "Psaumes du " + dayOfWeek.name))
        list.add(LiturgicalItem.Rubric("Antiphona:"))
        list.add(LiturgicalItem.BilingualText(antiphonLatin, antiphonVernacular))

        when (dayOfWeek) {
            DayOfWeek.SUNDAY -> {
                list.add(LiturgicalItem.Title("Psalmus 4", "Psaume 4", "Cum invocárem"))
                list.add(
                    LiturgicalItem.BilingualText(
                        latin = "1. Cum invocárem exaudívit me Deus justítiae meae : in tribulatióne dilatásti mihi.\n2. Miserére mei, et exáudi oratiónem meam.\n3. Fílii hóminum, úsquequo gravi corde ? ut quid dilígitis vanitátem, et quaéritis mendácium ?\n4. Et scitóte quóniam mirificávit Dóminus sanctum suum : Dóminus exáudiet me cum clamávero ad eum.\n5. Irascímini, et nolíte peccáre : quae dícitis in córdibus vestris, in cubílibus vestris compungímini.\n6. Sacrificáte sacrifícium justítiae, et speráte in Dómino : multi dicunt : Quis osténdit nobis bona ?\n7. Signátum est super nos lumen vultus tui, Dómine : dedísti laetítiam in corde meo.\n8. A fructu fruménti, vini et ólei sui multiplicáti sunt.\n9. In pace in idípsum dórmiam, et requiéscam ;\n10. Quóniam tu, Dómine, singuláriter in spe constituísti me.",
                        vernacular = "1. Quand je t'invoque, exauce-moi, Dieu de ma justice : dans l'angoisse, tu m'as mis au large.\n2. Aie pitié de moi, et exauce ma prière.\n3. Fils des hommes, jusques à quand aurez-vous le cœur appesanti ? Pourquoi aimez-vous la vanité et cherchez-vous le mensonge ?\n4. Sachez que le Seigneur a glorifié son saint : le Seigneur m'exaucera quand je crierai vers lui.\n5. Tremblez, et ne péchez point ; ce que vous dites dans vos cœurs, regrettez-le sur vos couches.\n6. Offrez des sacrifices de justice, et espérez dans le Seigneur : beaucoup disent : Qui nous fera voir le bonheur ?\n7. La lumière de ton visage a brillé sur nous, Seigneur : tu as mis la joie dans mon cœur.\n8. Par le froment, le vin et l'huile, leurs biens se sont multipliés.\n9. Dans la paix, je me coucherai et m'endormirai aussitôt ;\n10. Car toi seul, Seigneur, me fais reposer en sécurité.",
                        dropCap = true
                    )
                )
                list.add(LiturgicalItem.Title("Psalmus 90", "Psaume 90", "Qui hábitat"))
                list.add(
                    LiturgicalItem.BilingualText(
                        latin = "1. Qui hábitat in adjutório Altíssimi, in protectióne Dei caeli commorábitur.\n2. Dicet Dómino : Suscéptor meus es tu, et refúgium meum : Deus meus, sperábo in eum.\n3. Quóniam ipse liberávit me de láqueo venántium, et a verbo áspero.\n4. Scápulis suis obumbrábit tibi : et sub pennis ejus sperábis.\n5. Scuto circúmdabit te véritas ejus : non timébis a timóre noctúrno,\n6. A sagítta volánte in die, a negótio perambulánte in ténebris : ab incúrsu, et daemónio meridiáno.\n7. Cadent a látere tuo mille, et decem míllia a dextris tuis : ad te autem non appropinquábit.",
                        vernacular = "1. Celui qui habite sous l'abri du Très-Haut repose à l'ombre du Tout-Puissant.\n2. Je dis au Seigneur : Mon refuge et ma forteresse, mon Dieu en qui je me confie !\n3. Car c'est lui qui te délivre du filet de l'oiseleur, de la peste et de ses ravages.\n4. Il te couvrira de ses plumes, et tu trouveras un refuge sous ses ailes.\n5. Sa fidélité est un bouclier et une cuirasse : tu ne craindras ni les terreurs de la nuit,\n6. Ni la flèche qui vole de jour, ni la peste qui marche dans les ténèbres, ni la contagion qui frappe en plein midi.\n7. Que mille tombent à ton côté, et dix mille à ta droite, tu ne seras pas atteint.",
                        dropCap = true
                    )
                )
                list.add(LiturgicalItem.Title("Psalmus 133", "Psaume 133", "Ecce nunc"))
                list.add(
                    LiturgicalItem.BilingualText(
                        latin = "1. Ecce nunc benedícite Dóminum, omnes servi Dómini :\n2. Qui statis in domo Dómini, in átriis domus Dei nostri.\n3. In nóctibus extóllite manus vestras in sancta, et benedícite Dóminum.\n4. Benedícat te Dóminus ex Sion, qui fecit caelum et terram.",
                        vernacular = "1. Voici maintenant, bénissez le Seigneur, vous tous serviteurs du Seigneur :\n2. Qui vous tenez dans la maison du Seigneur, dans les parvis de la maison de notre Dieu.\n3. Pendant les nuits, élevez vos mains vers le sanctuaire, et bénissez le Seigneur.\n4. Que le Seigneur te bénisse depuis Sion, lui qui a fait le ciel et la terre.",
                        dropCap = true
                    )
                )
            }
            else -> {
                list.add(LiturgicalItem.Title("Psalmi Feriales", "Psaumes Fériaux", "Psalmodia ferialis"))
                list.add(
                    LiturgicalItem.BilingualText(
                        latin = "Psalmus 6 & 7:\n1. Dómine, ne in furóre tuo árguas me, neque in ira tua corrípias me.\n2. Miserére mei, Dómine, quóniam infírmus sum : sana me, Dómine, quóniam conturbáta sunt ossa mea.\n3. Dómine Deus meus, in te sperávi : salvum me fac ex ómnibus persequéntibus me, et líbera me.",
                        vernacular = "Psaume 6 et 7:\n1. Seigneur, ne me punis pas dans ta colère, et ne me châtie pas dans ta fureur.\n2. Aie pitié de moi, Seigneur, car je suis sans force ; guéris-moi, Seigneur, car mes os sont tremblants.\n3. Seigneur mon Dieu, en toi j'ai mis mon espérance : sauve-moi de tous mes persécuteurs, et délivre-moi.",
                        dropCap = true
                    )
                )
            }
        }

        list.add(LiturgicalItem.Rubric("Repetitur Antiphona:"))
        list.add(LiturgicalItem.BilingualText(antiphonLatin, antiphonVernacular))

        return list
    }

    private fun getMarianAntiphonItems(antiphon: MarianAntiphon): List<LiturgicalItem> {
        val list = mutableListOf<LiturgicalItem>()
        list.add(LiturgicalItem.HeaderSection("Antiphona Finalis B.M.V.", antiphon.frenchTitle))

        when (antiphon) {
            MarianAntiphon.ALMA_REDEMPTORIS_MATER -> {
                list.add(
                    LiturgicalItem.ChantScore(
                        title = "Alma Redemptoris Mater",
                        mode = "V",
                        gabc = """name: Alma Redemptoris Mater;
office-part: Antiphona;
mode: 5;
book: Antiphonale Monasticum, 1934, p. 176 & The Liber Usualis, 1961, p. 273;
transcriber: Andrew Hinkley;
%%
(c3) AL(f)ma(e) * Red(fh)emp(h)tó(h)ris(h) Ma(fe)ter,(f.) (;) quae(h) pér(h!ij)vi(i)a(h) cae(hi)li(h) por(fe)ta(f) ma(e.)nes,(e.) (:) et(f) stel(e)la(d) ma(ef)ris,(f.) (;) suc(h)cúr(hg)re(f) ca(gh)dén(f)ti(e.) (;) súr(f)ge(e)re(d) qui(ef) cu(f)rat(e) pó(d)pu(cd)lo :(d.) (::)
Tu(h) quae(h!ij) ge(i)nu(h)ís(hi)ti,(h.) (;) na(h)tú(fe)ra(f) mi(hg)rán(f)te,(e.) (;) tu(f)um(e) sanc(d)tum(ef) Ge(f)ni(e)tó(d.)rem :(d.) (::)
Vir(h!ij)go(i) pri(hg)us(f) ac(gh) pos(f)té(e)ri(f)us,(h.) (;) Ga(h)bri(h)é(fe)lis(f) ab(h) o(h!ij)re(i.) (;) su(h)mens(g) il(fe)lud(f) A(hg)ve,(f.) (;) pec(h)ca(g)tó(f)rum(e) mi(f)se(h)ré(fe)re.(d.) (::)""",
                        translation = "Sainte Mère du Rédempteur, Porte du ciel toujours ouverte, Étoile de la mer, viens au secours du peuple qui tombe et cherche à se relever."
                    )
                )
            }
            MarianAntiphon.AVE_REGINA_CAELORUM -> {
                list.add(
                    LiturgicalItem.ChantScore(
                        title = "Ave Regina Caelorum",
                        mode = "VI",
                        gabc = """name: Ave Regina Caelorum;
office-part: Antiphona;
mode: 6;
book: Antiphonale Monasticum, 1934, p. 177 & The Liber Usualis, 1961, p. 274;
transcriber: Andrew Hinkley;
%%
(c4) A(f)ve(e) Re(d)gí(c)na(d) cae(f)ló(g)rum,(f.) *(;) A(h)ve(j) Dó(ixi)mi(h)na(g) An(h)ge(g)ló(f)rum :(h.) (:) Sal(f)ve(e) ra(d)dix,(c) sal(d)ve(f) por(g)ta,(f.) (;) Ex(h) qua(g) mun(f)do(e) lux(g) est(fe) or(d.)ta :(d.) (::)
Gau(f)de(f) Vir(gh)go(h) glo(g)ri(h)ó(ixi)sa,(h.) (;) Su(j)per(ixi) om(h)nes(g) spe(f)ci(e)ó(g)sa :(h.) (:) Va(f)le,(e) o(d) val(c)de(d) de(f)có(g)ra,(f.) (;) Et(h) pro(g) no(fe)bis(d) Chris(g)tum(fe) ex(c)ó(d.)ra.(d.) (::)""",
                        translation = "Salut, Reine des cieux ! Salut, Souveraine des Anges ! Salut, tige de Jessé ! Salut, porte par qui la lumière s'est levée sur le monde !"
                    )
                )
            }
            MarianAntiphon.REGINA_CAELI -> {
                list.add(
                    LiturgicalItem.ChantScore(
                        title = "Regina Caeli (Temps Pascal)",
                        mode = "VI",
                        gabc = """name: Regina caeli;
office-part: Antiphona;
mode: 6;
book: Antiphonale Monasticum, 1934, p. 179 & The Liber Usualis, 1961, p. 275;
transcriber: Andrew Hinkley;
%%
(c4) RE(f)gí(g)na(h) cae(ixi)li(h.) * lae(g)tá(h)re,(f.) al(g)le(h)lú(g.)ia :(f.) (;) Qui(f)a(j) quem(j) me(ixi)ru(h)ís(g)ti(h) por(ixi)tá(h.)re,(f.) (;) al(g)le(h)lú(g.)ia :(f.) (:)
Re(j)sur(j)réx(ixi)it,(h) sic(g)ut(f) dix(g)it,(h.) al(g)le(h)lú(g.)ia :(f.) (;) O(j)ra(j) pro(ixi) no(h)bis(g) De(ixi)um,(h.) (;) al(g)le(f)lú(ixghGF~)(g)ia.(f.) (::)""",
                        translation = "Reine du ciel, réjouis-toi, alléluia ! Car celui que tu as mérité de porter, alléluia, est ressuscité comme il l'avait dit, alléluia ! Prie Dieu pour nous, alléluia !"
                    )
                )
            }
            MarianAntiphon.SALVE_REGINA -> {
                list.add(
                    LiturgicalItem.ChantScore(
                        title = "Salve Regina (Ton simple)",
                        mode = "V",
                        gabc = """name: Salve Regina;
office-part: Antiphona;
mode: 5;
book: Antiphonale Monasticum, 1934, p. 179 & The Liber Usualis, 1961, p. 276;
transcriber: Andrew Hinkley;
%%
(c4) SAl(c)ve(e) Re(g)gí(h)na,(g.) * ma(h)ter(j) mi(i)se(h)ri(g)cór(h)di(g)ae :(g.) (;) Vi(j)ta,(g) dul(h)cé(f)do,(e.) et(d) spes(e) nos(f)tra,(ed) sal(c.)ve.(c.) (::)
Ad(g) te(h) cla(i)má(j)mus,(g.) éx(h)su(i)les(j) fí(h)li(g)i(f) He(g)vae.(g.) (::)
Ad(j) te(g) sus(h)pi(f)rá(g)mus,(h.) ge(d)mén(f)tes(g) et(f) flen(ed)tes(e.) in(d) hac(c) la(d)cri(e)má(f)rum(ed) val(c.)le.(c.) (::)
E(g)ia(h) er(i)go,(j) Ad(h)vo(g)cá(h)ta(j) nos(i)tra,(h.) il(j)los(g) tu(h)os(f) mi(e)se(d)ri(e)cór(f)des(ed) ó(c)cu(d)los(e) ad(f) nos(ed) con(c)vér(d.)te.(c.) (::)
Et(g) Je(hi)sum,(j) be(i)ne(h)díc(g)tum(h) fruc(j)tum(i) ven(h)tris(g) tu(h)i,(g.) no(c)bis(d) post(e) hoc(f) ex(ed)sí(c)li(d)um(e) os(f)tén(ed)de.(c.) (::)
O(e) cle(g)mens,(g.) (::) O(h) pi(j)a,(i.) (::) O(g) dul(h)cis(j) Vir(i)go(h) Ma(g)rí(fe)a.(d!ef!ed/c.) (::)""",
                        translation = "Salut, ô Reine, Mère de miséricorde, notre vie, notre douceur et notre espérance, salut ! Enfants d'Ève, exilés, nous crions vers toi ; vers toi nous soupirons, gémissant et pleurant dans cette vallée de larmes. Tourne donc vers nous, notre Avocate, tes yeux miséricordieux. Et après cet exil, montre-nous Jésus, le fruit béni de tes entrailles. Ô clémente, ô compatissante, ô douce Vierge Marie !"
                    )
                )
            }
        }

        return list
    }
}
