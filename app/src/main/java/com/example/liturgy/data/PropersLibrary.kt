package com.example.liturgy.data

import com.example.liturgy.model.LiturgicalItem
import com.example.liturgy.model.LiturgicalSeason

data class ProperSet(
    val id: String,
    val feastLatin: String,
    val feastVernacular: String,
    val season: LiturgicalSeason,
    val introit: ProperChant,
    val gradual: ProperChant,
    val alleluiaOrTract: ProperChant,
    val sequence: ProperChant? = null,
    val offertory: ProperChant,
    val communion: ProperChant
)

data class ProperChant(
    val part: String, // Intr., Grad., All., Tract., Seq., Offert., Comm.
    val latinTitle: String,
    val vernacularTitle: String,
    val mode: String,
    val gabc: String,
    val translation: String
) {
    fun toLiturgicalItem(): LiturgicalItem.ChantScore {
        return LiturgicalItem.ChantScore(
            title = "$part - $latinTitle",
            mode = mode,
            gabc = gabc,
            translation = translation
        )
    }
}

object PropersLibrary {

    // 1. Dominica I Adventus (1er Dimanche de l'Avent)
    val advent1 = ProperSet(
        id = "advent_1",
        feastLatin = "Dominica I Adventus",
        feastVernacular = "Ier Dimanche de l'Avent",
        season = LiturgicalSeason.ADVENT,
        introit = ProperChant(
            part = "Intr.",
            latinTitle = "Ad te levavi",
            vernacularTitle = "Vers toi j'élève mon âme",
            mode = "VIII",
            gabc = """name: Ad te levavi;
office-part: Introitus;
mode: 8;
book: Graduale Romanum, 1961, p. 1 & The Liber Usualis, 1961, p. 318;
transcriber: Andrew Hinkley;
commentary: Ps 24: 1-4;
%%
(c4) AD(d) te(f) le(g)vá(g)vi(ghg.) *(,) á(h)ni(g)mam(fg) me(gh/ih)am :(h.) (:) De(h)us(gf) me(gh)us(h.) (;) in(h) te(gf) con(g)fí(fe)do,(d.) (;) non(f) e(g)ru(gh)bés(h)cam :(h.) (:) ne(f)que(gh) ir(h)rí(h)de(g)ant(fe) me(d.) (;) in(g)i(f)mí(ef)ci(g) me(fvED)i.(d.) (::)
(z) <sp>V/</sp>. Vi(g)as(hj) tu(j)as,(j) Dó(j)mi(j)ne,(j) de(j)món(k)stra(j) mi(ih)hi :(jj) *(:) et(ig) sé(hj)mi(j)tas(j) tu(j)as(j) e(j)dó(j)ce(j) me.(h.) (::)""",
            translation = "Vers toi, Seigneur, j'élève mon âme : mon Dieu, en toi je me confie, que je n'aie pas à rougir, et que mes ennemis ne se moquent pas de moi. Ps. Fais-moi connaître tes voies, Seigneur, et enseigne-moi tes sentiers."
        ),
        gradual = ProperChant(
            part = "Grad.",
            latinTitle = "Universi qui te exspectant",
            vernacularTitle = "Tous ceux qui espèrent en toi",
            mode = "I",
            gabc = """name: Universi qui te exspectant;
office-part: Graduale;
mode: 1;
book: Graduale Romanum, 1961, p. 2 & The Liber Usualis, 1961, p. 319;
transcriber: Andrew Hinkley;
commentary: Ps 24: 3, 4;
%%
(c4) U(c)ni(d)vér(ixdh/iv)si,(h.) *(;) qui(h) te(g) ex(f)spéc(gh)tant,(h.) (;) non(h) con(gf)fun(ef)dén(g)tur(f) Dó(fe)mi(cd)ne.(d.) (::)
(z) <sp>V/</sp>. Vi(h)as(h) tu(h)as(gf) Dó(g)mi(h)ne(h.) (;) no(h)tas(ixhi) fac(h) mi(gf)hi :(f.) *(:) et(f) sé(f)mi(f)tas(f) tu(gh)as(h.) (;) e(h)dó(gf)ce(ef) me.(d.) (::)""",
            translation = "Tous ceux qui espèrent en toi ne seront point confondus, Seigneur. ℣. Fais-moi connaître tes voies, Seigneur, et enseigne-moi tes sentiers."
        ),
        alleluiaOrTract = ProperChant(
            part = "All.",
            latinTitle = "Ostende nobis",
            vernacularTitle = "Montre-nous, Seigneur, ta miséricorde",
            mode = "VIII",
            gabc = """name: Ostende nobis;
office-part: Alleluia;
mode: 8;
book: Graduale Romanum, 1961, p. 2 & The Liber Usualis, 1961, p. 320;
transcriber: Andrew Hinkley;
commentary: Ps 84: 8;
%%
(c4) AL(g)le(gg)lú(h)ia.(ghg.) *(;) <i>ij.</i>(hvGF/ghg) (::)
(z) <sp>V/</sp>. Os(g)tén(g)de(f) no(gh)bis(h) Dó(gh)mi(g)ne(g.) (;) mi(g)se(h)ri(j)cór(k)di(j)am(ij) tu(h)am :(g.) *(:) et(g) sa(h)lu(j)tá(k)re(j) tu(ij)um(h.) (;) da(hvGF/ghg) no(g)bis.(g.) (::)""",
            translation = "Alléluia. ℣. Montre-nous, Seigneur, ta miséricorde, et donne-nous ton salut."
        ),
        offertory = ProperChant(
            part = "Offert.",
            latinTitle = "Ad te levavi",
            vernacularTitle = "Vers toi j'élève mon âme",
            mode = "II",
            gabc = """name: Ad te levavi (Offert.);
office-part: Offertorium;
mode: 2;
book: Graduale Romanum, 1961, p. 3 & The Liber Usualis, 1961, p. 320;
transcriber: Andrew Hinkley;
commentary: Ps 24: 1-3;
%%
(c4) AD(f) te(gh) Dó(h)mi(g)ne(g.) *(,) le(f)vá(ixgi)vi(h) á(h)ni(g)mam(f) me(gh)am :(h.) (:) De(h)us(gf) me(gh)us,(h.) (;) in(h) te(gf) con(g)fí(fe)do,(d.) (;) non(f) e(g)ru(gh)bés(h)cam.(h.) (::)""",
            translation = "Vers toi, Seigneur, j'élève mon âme : mon Dieu, en toi je me confie, que je ne sois pas confondu."
        ),
        communion = ProperChant(
            part = "Comm.",
            latinTitle = "Dominus dabit benignitatem",
            vernacularTitle = "Le Seigneur donnera sa bénédiction",
            mode = "I",
            gabc = """name: Dominus dabit;
office-part: Communio;
mode: 1;
book: Graduale Romanum, 1961, p. 3 & The Liber Usualis, 1961, p. 321;
transcriber: Andrew Hinkley;
commentary: Ps 84: 13;
%%
(c4) DO(f)mi(fe)nus(d) da(f)bit(g) be(f)ni(fe)gni(c)tá(d)tem :(d.) *(;) et(f) ter(fe)ra(d) nos(f)tra(g.) (;) da(h)bit(g) fruc(fe)tum(fg) su(f)um.(d.) (::)""",
            translation = "Le Seigneur donnera sa bénédiction, et notre terre donnera son fruit."
        )
    )

    // 2. In Nativitate Domini (Jour de Noël - Messe du Jour)
    val christmasDay = ProperSet(
        id = "christmas_day",
        feastLatin = "In Nativitate Domini (Missa in Die)",
        feastVernacular = "Fête de la Nativité du Seigneur (Messe du Jour)",
        season = LiturgicalSeason.CHRISTMASTIDE,
        introit = ProperChant(
            part = "Intr.",
            latinTitle = "Puer natus est nobis",
            vernacularTitle = "Un enfant nous est né",
            mode = "VII",
            gabc = """name: Puer natus est nobis;
office-part: Introitus;
mode: 7;
book: Graduale Romanum, 1961, p. 33 & The Liber Usualis, 1961, p. 408;
transcriber: Andrew Hinkley;
commentary: Is 9: 6; Ps 97: 1;
%%
(c3) PU(e)er(eh) na(h)tus(h) est(f) no(gh)bis,(g.) *(,) et(e) fí(g)li(h)us(i) da(i)tus(h) est(ij) no(ih)bis :(h.) (:) cu(hg)jus(f) im(g)pé(h)ri(g)um(f) (;) su(f)per(h) hú(h)me(g)rum(f) e(ef)jus :(e.) (:) et(e) vo(e)cá(ef)bi(e)tur(d) no(fh)men(h) e(h!ij)jus,(i.) (;) ma(h)gni(g) con(fe)sí(f)li(g)i(f) An(ef)ge(e)lus.(e.) (::)
(z) <sp>V/</sp>. Can(e)tá(eh)te(h) Dó(h)mi(h)no(h) cán(h)ti(h)cum(h) no(h)vum :(i.) *(:) qui(h)a(h) mi(h)ra(h)bí(h)li(g)a(f) fe(gh)cit.(h.) (::)""",
            translation = "Un enfant nous est né, et un fils nous a été donné ; l'empire a été posé sur son épaule, et on l'appellera : Ange du grand conseil. Ps. Chantez au Seigneur un cantique nouveau, car il a fait des merveilles."
        ),
        gradual = ProperChant(
            part = "Grad.",
            latinTitle = "Viderunt omnes",
            vernacularTitle = "Toutes les extrémités de la terre",
            mode = "V",
            gabc = """name: Viderunt omnes;
office-part: Graduale;
mode: 5;
book: Graduale Romanum, 1961, p. 34 & The Liber Usualis, 1961, p. 409;
transcriber: Andrew Hinkley;
commentary: Ps 97: 3-4, 2;
%%
(c4) VI(d)dé(ffg)runt(f) om(gh)nes(h.) *(;) fi(h)nes(g) ter(fe)rae(d.) (;) sa(f)lu(g)tá(h)re(g) De(fe)i(fg) no(f)stri :(f.) (:) ju(f)bi(f)lá(gh)te(h) De(g)o,(f.) (;) om(fg)nis(fe) ter(cd)ra.(d.) (::)
(z) <sp>V/</sp>. No(f)tum(gh) fe(h)cit(h) Dó(h)mi(h)nus(h) sa(h)lu(g)tá(fe)re(fg) su(f)um :(f.) *(:) an(f)te(f) con(gh)spéc(h)tum(h) gén(h)ti(g)um(f.) (;) re(f)ve(fe)lá(d)vit(c) jus(ef)tí(g)ti(f)am(fe) su(cd)am.(d.) (::)""",
            translation = "Toutes les extrémités de la terre ont vu le salut de notre Dieu : poussez des cris de joie vers Dieu, toute la terre. ℣. Le Seigneur a fait connaître son salut : à la face des nations, il a révélé sa justice."
        ),
        alleluiaOrTract = ProperChant(
            part = "All.",
            latinTitle = "Dies sanctificatus",
            vernacularTitle = "Un jour saint a brillé",
            mode = "II",
            gabc = """name: Dies sanctificatus;
office-part: Alleluia;
mode: 2;
book: Graduale Romanum, 1961, p. 35 & The Liber Usualis, 1961, p. 409;
transcriber: Andrew Hinkley;
%%
(c4) AL(cd)le(f)lú(fg)ia.(f.) *(;) <i>ij.</i>(e/ghg/fe/ddc) (::)
(z) <sp>V/</sp>. Di(f)es(f) sanc(gh)ti(h)fi(g)cá(fe)tus(fg) il(g)lú(f)xit(e) no(cd)bis :(d.) *(:) ve(f)ní(gh)te(h) gen(h)tes,(g.) (;) et(f) ad(fe)o(d)rá(ef)te(g) Dó(fe)mi(cd)num.(d.) (::)""",
            translation = "Alléluia. ℣. Un jour sanctifié s'est levé pour nous : venez, nations, et adorez le Seigneur."
        ),
        offertory = ProperChant(
            part = "Offert.",
            latinTitle = "Tui sunt caeli",
            vernacularTitle = "À toi sont les cieux",
            mode = "IV",
            gabc = """name: Tui sunt caeli;
office-part: Offertorium;
mode: 4;
book: Graduale Romanum, 1961, p. 35 & The Liber Usualis, 1961, p. 410;
transcriber: Andrew Hinkley;
commentary: Ps 88: 12, 15;
%%
(c4) TU(df)i(f) sunt(gh) cae(gf)li,(e.) *(,) et(fg) tu(fe)a(d) est(cd) ter(de)ra :(d.) (:) or(f)bem(gh) ter(h)rá(g)rum,(fe) (;) et(fg) ple(f)ni(f)tú(e)di(d)nem(c) e(de)jus(e.) (;) tu(fd) fun(f/gh)dás(g)ti.(e.) (::)""",
            translation = "À toi sont les cieux et à toi est la terre : l'orbe de la terre et tout ce qu'il contient, c'est toi qui les as fondés."
        ),
        communion = ProperChant(
            part = "Comm.",
            latinTitle = "Viderunt omnes",
            vernacularTitle = "Toutes les extrémités de la terre",
            mode = "I",
            gabc = """name: Viderunt omnes (Comm.);
office-part: Communio;
mode: 1;
book: Graduale Romanum, 1961, p. 36 & The Liber Usualis, 1961, p. 410;
transcriber: Andrew Hinkley;
commentary: Ps 97: 3;
%%
(c4) VI(f)dé(f)runt(fe) om(d)nes(c.) *(,) fi(f)nes(gh) ter(ixhi)rae(h.) (;) sa(h)lu(g)tá(fe)re(fg) De(f)i(fED) no(cd)stri.(d.) (::)""",
            translation = "Toutes les extrémités de la terre ont vu le salut de notre Dieu."
        )
    )

    // 3. In Epiphania Domini (Épiphanie - 6 Janvier)
    val epiphany = ProperSet(
        id = "epiphany",
        feastLatin = "In Epiphania Domini",
        feastVernacular = "Épiphanie de Notre-Seigneur",
        season = LiturgicalSeason.TIME_AFTER_EPIPHANY,
        introit = ProperChant(
            part = "Intr.",
            latinTitle = "Ecce advenit",
            vernacularTitle = "Voici qu'arrive le Souverain Seigneur",
            mode = "II",
            gabc = """name: Ecce advenit;
office-part: Introitus;
mode: 2;
book: Graduale Romanum, 1961, p. 57 & The Liber Usualis, 1961, p. 459;
transcriber: Andrew Hinkley;
commentary: Mal 3: 1; 1 Chron 29: 12; Ps 71: 1;
%%
(c3) EC(c)ce(e) ad(f)vé(f)nit(fe) *(,) do(f)mi(h)ná(h)tor(gh) Dó(f)mi(ef)nus :(f.) (:) et(f) re(h)gnum(h) in(h) ma(h)nu(i) e(hg)jus,(f.) (;) et(f) po(h)tés(h)tas,(gh) (;) et(f) im(e)pé(fh)ri(h)um.(f.) (::)
(z) <sp>V/</sp>. De(e)us(f) ju(h)dí(h)ci(h)um(h) tu(h)um(h) Re(h)gi(i) da :(h.) *(:) et(h) jus(h)tí(h)ti(h)am(h) tu(h)am(h) Fí(h)li(g)o(f) Re(gh)gis.(f.) (::)""",
            translation = "Voici qu'arrive le Souverain Seigneur : la royauté est dans sa main, avec la puissance et l'empire. Ps. Ô Dieu, donne tes jugements au Roi, et ta justice au Fils du Roi."
        ),
        gradual = ProperChant(
            part = "Grad.",
            latinTitle = "Omnes de Saba",
            vernacularTitle = "Tous ceux de Saba viendront",
            mode = "V",
            gabc = """name: Omnes de Saba;
office-part: Graduale;
mode: 5;
book: Graduale Romanum, 1961, p. 58 & The Liber Usualis, 1961, p. 459;
transcriber: Andrew Hinkley;
commentary: Is 60: 6, 1;
%%
(c4) OM(d)nes(ffg) de(f) Sa(gh)ba(h.) *(;) vé(h)ni(g)ent,(fe) (;) au(f)rum(g) et(h) thus(g) de(fe)fe(fg)rén(f)tes,(f.) (:) et(f) lau(f)dem(gh) Dó(h)mi(g)no(f.) (;) an(fg)nun(fe)ti(cd)án(d)tes.(d.) (::)
(z) <sp>V/</sp>. Sur(f)ge,(gh) et(h) il(h)lu(h)mi(g)ná(fe)re(fg) Je(f)rú(f)sa(gh)lem :(h.) *(:) qui(h)a(h) gló(h)ri(g)a(f) Dó(g)mi(f)ni(fe) (;) su(d)per(c) te(ef) or(g)ta(fe) est.(cd/ed) (::)""",
            translation = "Tous ceux de Saba viendront, apportant l'or et l'encens, et proclamant les louanges du Seigneur. ℣. Lève-toi, resplendis, Jérusalem, car la gloire du Seigneur s'est levée sur toi."
        ),
        alleluiaOrTract = ProperChant(
            part = "All.",
            latinTitle = "Vidimus stellam",
            vernacularTitle = "Nous avons vu son étoile",
            mode = "IV",
            gabc = """name: Vidimus stellam;
office-part: Alleluia;
mode: 4;
book: Graduale Romanum, 1961, p. 59 & The Liber Usualis, 1961, p. 460;
transcriber: Andrew Hinkley;
commentary: Mt 2: 2;
%%
(c4) AL(fd~)le(e!fg)lú(gh)ia.(h.) *(;) <i>ij.</i>(ixhiHG/hff/ded) (::)
(z) <sp>V/</sp>. Ví(h)di(gh)mus(h) stel(h)lam(g) e(fe)jus(fg) in(g) O(f)ri(e)én(cd)te,(d.) *(:) et(d) vé(fe)ni(fg)mus(g) cum(gh) mu(g)né(fe)ri(dc)bus(c.) (;) ad(d)o(fe)rá(fg)re(f) Dó(e)mi(cd)num.(d.) (::)""",
            translation = "Alléluia. ℣. Nous avons vu son étoile en Orient, et nous sommes venus avec des présents adorer le Seigneur."
        ),
        offertory = ProperChant(
            part = "Offert.",
            latinTitle = "Reges Tharsis",
            vernacularTitle = "Les rois de Tharsis offriront des dons",
            mode = "V",
            gabc = """name: Reges Tharsis;
office-part: Offertorium;
mode: 5;
book: Graduale Romanum, 1961, p. 59 & The Liber Usualis, 1961, p. 461;
transcriber: Andrew Hinkley;
commentary: Ps 71: 10-11;
%%
(c4) RE(ffg)ges(f) Thar(gh)sis(h.) *(,) et(h) ín(g)su(fe)lae(d.) (;) mú(f)ne(g)ra(h) óf(g)fe(fe)rent :(fg/f.) (:) re(f)ges(gh) A(h)ra(g)bum(fe) et(fg) Sa(f)ba(f.) (;) do(fED)na(cd) ad(d)dú(de)cent.(d.) (::)""",
            translation = "Les rois de Tharsis et les îles offriront des présents : les rois d'Arabie et de Saba apporteront des dons."
        ),
        communion = ProperChant(
            part = "Comm.",
            latinTitle = "Vidimus stellam",
            vernacularTitle = "Nous avons vu son étoile",
            mode = "IV",
            gabc = """name: Vidimus stellam (Comm.);
office-part: Communio;
mode: 4;
book: Graduale Romanum, 1961, p. 60 & The Liber Usualis, 1961, p. 462;
transcriber: Andrew Hinkley;
commentary: Mt 2: 2;
%%
(c4) VI(fe)di(d)mus(e) stel(f)lam(g) e(gh)jus(h.) *(,) in(g) O(f)ri(gh)én(gvFE)te,(e.) (:) et(f) vé(gh)ni(g)mus(e) cum(f) mu(d)né(e)ri(c)bus(c.) (;) ad(d)o(fe)rá(fg)re(f) Dó(e)mi(de)num.(e.) (::)""",
            translation = "Nous avons vu son étoile en Orient, et nous sommes venus avec des présents adorer le Seigneur."
        )
    )

    // 4. Dominica I in Quadragesima / Cendres (Carême)
    val lent1 = ProperSet(
        id = "lent_1",
        feastLatin = "Dominica I in Quadragesima",
        feastVernacular = "Ier Dimanche de Carême",
        season = LiturgicalSeason.LENT,
        introit = ProperChant(
            part = "Intr.",
            latinTitle = "Invocabit me",
            vernacularTitle = "Il m'invoquera et je l'exaucerai",
            mode = "VIII",
            gabc = """name: Invocabit me;
office-part: Introitus;
mode: 8;
book: Graduale Romanum, 1961, p. 91 & The Liber Usualis, 1961, p. 532;
transcriber: Andrew Hinkley;
commentary: Ps 90: 15-16, 1;
%%
(c4) IN(d)vo(f)cá(g)bit(g) me,(ghg.) *(,) et(f) e(h)go(j) e(j)xáu(i)di(h)am(g) e(h!ij)um :(g.) (:) e(h)rí(j)pi(i)am(h) e(hg)um,(f.) (;) et(f) glo(h)ri(g)fi(f)cá(e)bo(d) e(g)um :(g.) (:) lon(g)gi(h)tú(j)di(i)ne(h) di(g)é(h)rum(g.) (;) ad(f)im(g)plé(h)bo(g) e(fg)um.(g.) (::)
(z) <sp>V/</sp>. Qui(g) há(hj)bi(j)tat(j) in(j) ad(j)ju(j)tó(j)ri(j)o(j) Al(k)tís(j)si(ih)mi :(jj) *(:) in(ig) pro(hj)tec(j)ti(j)ó(j)ne(j) De(j)i(j) cae(j)li(j) com(j)mo(j)rá(j)bi(h)tur.(g.) (::)""",
            translation = "Il m'invoquera, et je l'exaucerai ; je le délivrerai et je le glorifierai ; je le comblerai de longs jours. Ps. Celui qui habite sous l'assistance du Très-Haut, demeurera sous la protection du Dieu du ciel."
        ),
        gradual = ProperChant(
            part = "Grad.",
            latinTitle = "Angelis suis",
            vernacularTitle = "À ses anges il a donné des ordres",
            mode = "II",
            gabc = """name: Angelis suis;
office-part: Graduale;
mode: 2;
book: Graduale Romanum, 1961, p. 92 & The Liber Usualis, 1961, p. 533;
transcriber: Andrew Hinkley;
commentary: Ps 90: 11-12;
%%
(c4) AN(f)ge(fe)lis(d) su(f)is(g) * man(h)dá(g)vit(f) de(gh) te,(h.) (:) ut(h) cus(h)tó(g)di(f)ant(ef) te(d.) (;) in(f) óm(g)ni(f)bus(e) vi(d)is(c) tu(de)is.(d.) (::)
(z) <sp>V/</sp>. In(f) má(gh)ni(h)bus(h) por(h)tá(h)bunt(g) te :(fe) *(:) ne(d) un(f)quam(g) of(h)fén(g)das(f) (;) ad(f) lá(fe)pi(d)dem(c) pe(ef)dem(g) tu(fe)um.(cd) (::)""",
            translation = "Il a commandé à ses anges de te garder dans toutes tes voies. ℣. Ils te porteront sur leurs mains, de peur que ton pied ne heurte contre la pierre."
        ),
        alleluiaOrTract = ProperChant(
            part = "Tract.",
            latinTitle = "Qui habitat in adjutorio",
            vernacularTitle = "Celui qui demeure sous l'abri du Très-Haut",
            mode = "II",
            gabc = """name: Qui habitat;
office-part: Tractus;
mode: 2;
book: Graduale Romanum, 1961, p. 93 & The Liber Usualis, 1961, p. 533;
transcriber: Andrew Hinkley;
commentary: Ps 90: 1-7;
%%
(c4) QUI(f) há(fe)bi(d)tat(f) * in(g) ad(h)ju(g)tó(f)ri(g)o(f) Al(f)tís(e)si(d)mi,(d.) (:) in(f) pro(gh)tec(h)ti(g)ó(f)ne(f) De(fg)i(f) cae(fe)li(d.) (;) com(c)mo(ef)rá(g)bi(fe)tur.(cd) (::)""",
            translation = "Celui qui habite sous l'assistance du Très-Haut, reposera sous la protection du Dieu du ciel."
        ),
        offertory = ProperChant(
            part = "Offert.",
            latinTitle = "Scapulis suis",
            vernacularTitle = "De ses épaules il te couvrira",
            mode = "VIII",
            gabc = """name: Scapulis suis;
office-part: Offertorium;
mode: 8;
book: Graduale Romanum, 1961, p. 94 & The Liber Usualis, 1961, p. 537;
transcriber: Andrew Hinkley;
commentary: Ps 90: 4-5;
%%
(c4) SCA(g)pu(h)lis(g) su(h)is(g.) *(,) ob(f)um(h)brá(j)bit(i) ti(h)bi(g) Dó(h)mi(g)nus,(f.) (:) et(f) sub(h) pen(j)nis(i) e(jk)jus(h.) (;) spe(gf)rá(gh/ih)bis :(h.) (:) scu(j)to(i) cir(h)cúm(g)da(h)bit(g) te(f.) (;) vé(e)ri(f)tas(g) e(fvED)jus.(d.) (::)""",
            translation = "Le Seigneur te couvrira de ses épaules, et sous ses ailes tu auras confiance : sa vérité t'environnera comme un bouclier."
        ),
        communion = ProperChant(
            part = "Comm.",
            latinTitle = "Scapulis suis",
            vernacularTitle = "De ses épaules",
            mode = "III",
            gabc = """name: Scapulis suis (Comm.);
office-part: Communio;
mode: 3;
book: Graduale Romanum, 1961, p. 95 & The Liber Usualis, 1961, p. 538;
transcriber: Andrew Hinkley;
commentary: Ps 90: 4-5;
%%
(c4) SCA(e)pu(f)lis(ed) su(g)is(h.) *(,) ob(i)um(j)brá(h)bit(i) ti(h)bi,(g.) (;) et(i) sub(j) pen(h)nis(g) e(e)jus(f) spe(gh)rá(g)bis :(e.) (:) scu(g)to(h) cir(i)cúm(j)da(i)bit(h) te(g.) (;) vé(h)ri(g)tas(fe) e(de)jus.(e.) (::)""",
            translation = "Il te couvrira de ses épaules, et sous ses ailes tu espéreras : sa fidélité est un bouclier et une cuirasse."
        )
    )

    // 5. Dominica Resurrectionis (Pâques)
    val easterSunday = ProperSet(
        id = "easter_sunday",
        feastLatin = "Dominica Resurrectionis (In Die Sancto Paschae)",
        feastVernacular = "Dimanche de la Résurrection (Jour de Pâques)",
        season = LiturgicalSeason.EASTERTIDE,
        introit = ProperChant(
            part = "Intr.",
            latinTitle = "Resurrexi",
            vernacularTitle = "Je suis ressuscité",
            mode = "IV",
            gabc = """name: Resurrexi;
office-part: Introitus;
mode: 4;
book: Graduale Romanum, 1961, p. 240 & The Liber Usualis, 1961, p. 778;
transcriber: Andrew Hinkley;
commentary: Ps 138: 18, 5-6, 1-2;
%%
(c4) RE(f)sur(df)ré(f)xi,(f.) *(,) et(f) ad(gh)huc(h) te(gf)cum(gh) sum,(h.) (;) al(g)le(f)lú(fe)ia :(d.) (:) po(f)su(df)í(f)sti(f) su(gh)per(h) me(h.) (;) ma(h)num(gf) tu(gh)am,(h.) (;) al(g)le(f)lú(fe)ia :(d.) (:) mi(d)rá(f)bi(g)lis(g) fac(h)ta(g) est(f) (;) sci(f)én(gh)ti(g)a(fe) tu(fg)a,(f.) (;) al(f)le(fd)lú(f/gh)ia,(g.) (;) al(g)le(fvED)lú(cd)ia.(d.) (::)
(z) <sp>V/</sp>. Dó(f)mi(gh)ne,(h) pro(h)bás(h)ti(h) me,(h) et(h) co(h)gno(h)vís(h)ti(h) me :(ixhi) *(:) tu(h) co(h)gno(h)vís(h)ti(h) ses(h)si(h)ó(h)nem(h) me(h)am,(h) et(h) re(h)sur(h)rec(h)ti(h)ó(g)nem(f) me(gh)am.(h.) (::)""",
            translation = "Je suis ressuscité et je suis encore avec toi, alléluia ; tu as posé sur moi ta main, alléluia ; ta science est devenue admirable, alléluia, alléluia. Ps. Seigneur, tu m'as sondé et tu m'as connu : tu as connu quand je m'assieds et quand je me lève."
        ),
        gradual = ProperChant(
            part = "Grad.",
            latinTitle = "Haec dies",
            vernacularTitle = "Voici le jour",
            mode = "II",
            gabc = """name: Haec dies;
office-part: Graduale;
mode: 2;
book: Graduale Romanum, 1961, p. 241 & The Liber Usualis, 1961, p. 778;
transcriber: Andrew Hinkley;
commentary: Ps 117: 24, 1;
%%
(c4) HAEC(ff) di(fg)es,(f.) *(;) quam(f) fe(gh)cit(h) Dó(hg)mi(f)nus :(f!gh) (:) ex(h)sul(h)té(hvGF)mus,(f.) (;) et(f) lae(f)té(fg)mur(f) in(fe) e(d)a.(d.) (::)
(z) <sp>V/</sp>. Con(f)fi(f)té(f)mi(f)ni(f) Dó(gh)mi(h)no,(h.) quó(hg)ni(f)am(gh) bo(g)nus :(f.) *(:) quó(f)ni(fe)am(d) in(e) saé(f)cu(e)lum(d.) (;) mi(d)se(f)ri(g)cór(h)di(g)a(fe) e(cd)jus.(d.) (::)""",
            translation = "Voici le jour qu'a fait le Seigneur : exultons et réjouissons-nous en lui. ℣. Rendez grâce au Seigneur car il est bon, car sa miséricorde est éternelle."
        ),
        alleluiaOrTract = ProperChant(
            part = "All.",
            latinTitle = "Pascha nostrum",
            vernacularTitle = "Le Christ notre Pâque",
            mode = "VII",
            gabc = """name: Pascha nostrum;
office-part: Alleluia;
mode: 7;
book: Graduale Romanum, 1961, p. 242 & The Liber Usualis, 1961, p. 779;
transcriber: Andrew Hinkley;
commentary: 1 Cor 5: 7;
%%
(c3) AL(e)le(ef)lú(h)ia.(h.) *(;) <i>ij.</i>(hg/hih) (::)
(z) <sp>V/</sp>. Pas(ef)cha(h) no(h)strum(h.) im(h)mo(h)lá(h)tus(g) est(f) Chris(g!hi)tus.(i.) (::)""",
            translation = "Alléluia. ℣. Le Christ, notre Pâque, a été immolé."
        ),
        sequence = ProperChant(
            part = "Seq.",
            latinTitle = "Victimae Paschali Laudes",
            vernacularTitle = "À la Victime Pascale",
            mode = "I",
            gabc = """name: Victimae paschali laudes;
office-part: Sequentia;
mode: 1;
book: Graduale Romanum, 1961, p. 242 & The Liber Usualis, 1961, p. 780;
transcriber: Andrew Hinkley;
%%
(c4) VIc(d)ti(c)mae(d) pas(f)chá(g)li(f) lau(e)des(d.) *(,) im(h)mo(g)lent(f) Chris(e)ti(d)á(c)ni.(d.) (::)
A(h)gnus(j) red(k)é(h)mit(g) o(h)ves :(h.) (;) Chris(h)tus(g) ín(h)no(g)cens(f) Pa(e)tri(d.) (;) re(f)con(g)ci(d)li(c)á(d)vit(f) pec(f)ca(e)tó(d)res.(d.) (::)
Mors(h) et(j) vi(k)ta(h) du(g)él(h)lo(h.) (;) con(h)fli(g)xé(h)re(g) mi(f)rán(e)do :(d.) (;) dux(f) vi(g)tae(d) mór(c)tu(d)us,(f) re(f)gnat(e) vi(d)vus.(d.) (::)
Dic(a) no(cd)bis(d) Ma(f)rí(ed)a,(c.) (;) quid(f) vi(e)dís(g)ti(h) in(g) vi(f)a ?(e.) (::)
Se(f)púl(e)crum(d) Chris(h)ti(g) vi(h)vén(gf~)tis,(e.) (;) et(e) gló(g)ri(h)am(h) vi(f)di(e) re(g)sur(fe)gén(d)tis.(d.) (::)
Al(e)le(g)lú(fe)ia.(d.) (::)""",
            translation = "À la Victime pascale, chrétiens, offrez vos louanges. L'Agneau a racheté les brebis : le Christ innocent a réconcilié les pécheurs avec le Père. La mort et la vie se sont affrontées en un duel admirable : le Maître de la vie, mort, règne vivant !"
        ),
        offertory = ProperChant(
            part = "Offert.",
            latinTitle = "Terra tremuit",
            vernacularTitle = "La terre a tremblé",
            mode = "IV",
            gabc = """name: Terra tremuit;
office-part: Offertorium;
mode: 4;
book: Graduale Romanum, 1961, p. 242 & The Liber Usualis, 1961, p. 781;
transcriber: Andrew Hinkley;
commentary: Ps 75: 9-10;
%%
(c4) TER(df)ra(f) tré(gh)mu(gf)it,(fe) *(,) et(fg) qui(f)é(fe)vit,(d.) (:) dum(f) re(gh)súr(h)ge(gf)ret(ef) in(g) ju(f)dí(fe)ci(d)o(cd) De(d)us,(d.) (;) al(f)le(fd)lú(f/gh)ia.(g.) (::)""",
            translation = "La terre a tremblé et s'est tue, lorsque Dieu s'est levé pour le jugement, alléluia."
        ),
        communion = ProperChant(
            part = "Comm.",
            latinTitle = "Pascha nostrum",
            vernacularTitle = "Le Christ notre Pâque",
            mode = "VI",
            gabc = """name: Pascha nostrum;
office-part: Communio;
mode: 6;
book: Graduale Romanum, 1961, p. 243 & The Liber Usualis, 1961, p. 781;
transcriber: Andrew Hinkley;
commentary: 1 Cor 5: 7-8;
%%
(c4) PAS(f)cha(fe) no(d)strum(c.) *(,) im(f)mo(g)lá(h)tus(g) est(f) Chris(gh)tus,(h.) al(g)le(f)lú(fe)ia :(d.) (:) í(f)ta(g)que(h) e(g)pu(f)lé(gh)mur(h.) (;) in(h) á(gf)zy(e)mis(f) sin(g)ce(e)ri(d)tá(e)tis(c.) (;) et(f) ve(fe)ri(d)tá(ef)tis,(f.) (;) al(f)le(fd)lú(f/gh)ia,(g.) (;) al(g)le(fvED)lú(cd)ia.(d.) (::)""",
            translation = "Le Christ, notre Pâque, a été immolé, alléluia : célébrons donc la fête avec les pains sans levain de la sincérité et de la vérité, alléluia, alléluia."
        )
    )

    // 6. In Ascensione Domini (Ascension)
    val ascension = ProperSet(
        id = "ascension",
        feastLatin = "In Ascensione Domini",
        feastVernacular = "Ascension de Notre-Seigneur",
        season = LiturgicalSeason.EASTERTIDE,
        introit = ProperChant(
            part = "Intr.",
            latinTitle = "Viri Galilaei",
            vernacularTitle = "Hommes de Galilée",
            mode = "VII",
            gabc = """name: Viri Galilaei;
office-part: Introitus;
mode: 7;
book: Graduale Romanum, 1961, p. 290 & The Liber Usualis, 1961, p. 846;
transcriber: Andrew Hinkley;
commentary: Act 1: 11; Ps 46: 2;
%%
(c3) VI(e)ri(eh) Ga(h)li(h)laé(h)i,(h.) *(,) quid(h) ad(h)mi(g)rá(f)mi(gh)ni(g.) (;) as(f)pi(h)ci(i)én(i)tes(h) in(ij) cae(ih)lum ?(h.) (:) al(g)le(f)lú(e!fh)ia :(h.) (:) quem(e)ád(f)mo(h)dum(h) vi(h)dís(h)tis(h) e(h)um(h.) (;) as(h)cen(i)dén(j)tem(i) in(h) cae(ij)lum,(ih..) (;) i(h)ta(g) vé(fe)ni(f)et,(e.) (;) al(e)le(fh)lú(hg)ia,(fe) (;) al(f)le(ef)lú(e.)ia.(e.) (::)
(z) <sp>V/</sp>. Om(e)nes(eh) gen(h)tes(h) pláu(h)di(h)te(h) má(h)ni(h)bus :(i.) *(:) ju(h)bi(h)lá(h)te(h) De(h)o(h) in(h) vo(h)ce(h) ex(h)sul(h)ta(g)ti(f)ó(gh)nis.(h.) (::)""",
            translation = "Hommes de Galilée, pourquoi regardez-vous le ciel avec étonnement ? alléluia : de la même manière que vous l'avez vu monter au ciel, il viendra, alléluia, alléluia. Ps. Vous toutes, nations, frappez des mains : poussez des cris de joie vers Dieu avec des chants d'allégresse."
        ),
        gradual = ProperChant(
            part = "All.",
            latinTitle = "Ascendit Deus",
            vernacularTitle = "Dieu est monté au milieu des acclamations",
            mode = "IV",
            gabc = """name: Ascendit Deus;
office-part: Alleluia;
mode: 4;
book: Graduale Romanum, 1961, p. 291 & The Liber Usualis, 1961, p. 847;
transcriber: Andrew Hinkley;
commentary: Ps 46: 6;
%%
(c4) AL(fd~)le(e!fg)lú(gh)ia.(h.) *(;) <i>ij.</i>(ixhiHG/hff/ded) (::)
(z) <sp>V/</sp>. As(h)cén(gh)dit(h) De(ixgiHG)us(gh) in(h) ju(g)bi(f)la(gh)ti(g)ó(fe)ne,(d.) *(:) et(d) Dó(fe)mi(fg)nus(g) in(gh) vo(f)ce(fe) tu(de)bae.(e.) (::)""",
            translation = "Alléluia. ℣. Dieu est monté au milieu des acclamations, et le Seigneur au son de la trompette."
        ),
        alleluiaOrTract = ProperChant(
            part = "All.",
            latinTitle = "Dominus in Sina",
            vernacularTitle = "Le Seigneur est dans le Sinaï",
            mode = "VIII",
            gabc = """name: Dominus in Sina;
office-part: Alleluia;
mode: 8;
book: Graduale Romanum, 1961, p. 292 & The Liber Usualis, 1961, p. 848;
transcriber: Andrew Hinkley;
commentary: Ps 67: 18-19;
%%
(c4) AL(g)le(gg)lú(h)ia.(ghg.) *(;) <i>ij.</i>(hvGF/ghg) (::)
(z) <sp>V/</sp>. Dó(g)mi(f)nus(gh) in(h) Si(gh)na(g) in(g) sanc(h)to,(j.) *(:) as(j)cén(ji)dens(h) in(g) al(gh)tum,(h.) (;) cap(g)tí(f)vam(e) du(f)xit(g) cap(g)ti(fe)vi(d)tá(ef)tem.(g.) (::)""",
            translation = "Alléluia. ℣. Le Seigneur est dans le Sinaï, dans le sanctuaire : montant au ciel, il a emmené des captifs."
        ),
        offertory = ProperChant(
            part = "Offert.",
            latinTitle = "Ascendit Deus",
            vernacularTitle = "Dieu est monté",
            mode = "I",
            gabc = """name: Ascendit Deus (Offert.);
office-part: Offertorium;
mode: 1;
book: Graduale Romanum, 1961, p. 292 & The Liber Usualis, 1961, p. 848;
transcriber: Andrew Hinkley;
commentary: Ps 46: 6;
%%
(c4) AS(f)cén(gh)dit(h) De(ixhi)us(h.) *(,) in(h) ju(g)bi(f)la(gh)ti(g)ó(fe)ne,(d.) (:) et(f) Dó(gh)mi(h)nus(h.) (;) in(h) vo(ixhi)ce(h) tu(g)bae,(fe) (;) al(g)le(fe)lú(d.)ia.(d.) (::)""",
            translation = "Dieu est monté au milieu des acclamations, et le Seigneur au son de la trompette, alléluia."
        ),
        communion = ProperChant(
            part = "Comm.",
            latinTitle = "Psallite Domino",
            vernacularTitle = "Chantez au Seigneur",
            mode = "I",
            gabc = """name: Psallite Domino;
office-part: Communio;
mode: 1;
book: Graduale Romanum, 1961, p. 293 & The Liber Usualis, 1961, p. 849;
transcriber: Andrew Hinkley;
commentary: Ps 67: 33-34;
%%
(c4) PSAL(f)li(fe)te(d) Dó(f)mi(g)no,(f.) *(,) qui(f) as(f)cén(gh)dit(h) su(h)per(g) cae(h)los(f) cae(gh)ló(fe)rum(d.) (;) ad(f) O(g)ri(f)én(fe)tem,(c.) (;) al(d)le(fe)lú(fg)ia.(f.) (::)""",
            translation = "Chantez au Seigneur, qui est monté au-dessus des cieux des cieux vers l'Orient, alléluia."
        )
    )

    // 7. Dominica Pentecostes (Pentecôte)
    val pentecost = ProperSet(
        id = "pentecost",
        feastLatin = "Dominica Pentecostes",
        feastVernacular = "Dimanche de la Pentecôte",
        season = LiturgicalSeason.EASTERTIDE,
        introit = ProperChant(
            part = "Intr.",
            latinTitle = "Spiritus Domini",
            vernacularTitle = "L'Esprit du Seigneur a rempli l'univers",
            mode = "VIII",
            gabc = """name: Spiritus Domini;
office-part: Introitus;
mode: 8;
book: Graduale Romanum, 1961, p. 294 & The Liber Usualis, 1961, p. 878;
transcriber: Andrew Hinkley;
commentary: Sap 1: 7; Ps 67: 2;
%%
(c4) SPÍ(g)RI(g)TUS(g) Dó(g)mi(g)ni(g.) *(,) re(g)plé(g)vit(g) or(h)bem(g) ter(f)rá(gh)rum,(h.) (;) al(g)le(fe)lú(fg/ag)ia :(g.) (:) et(g) hoc(j) quod(j) cón(i)ti(j)net(h) óm(j)ni(h)a,(g.) (;) sci(g)én(h)ti(g)am(f) ha(h)bet(j) vo(i)cis,(h.) (;) al(h)le(gf)lú(gh)ia,(g.) (;) al(h)le(fe)lú(fg/ag)ia.(g.) (::)
(z) <sp>V/</sp>. Ex(g)súr(hj)gat(j) De(j)us,(j) et(j) dis(j)si(j)pén(j)tur(j) in(j)i(j)mí(k)ci(j) e(ih)jus :(jj) *(:) et(ig) fú(hj)gi(j)ant,(j) qui(j) o(j)dé(j)runt(j) e(j)um,(j) a(j) fá(j)ci(j)e(j) e(j)jus.(h.) (::)""",
            translation = "L'Esprit du Seigneur a rempli l'univers, alléluia : et lui qui embrasse tout, a la connaissance de toute voix, alléluia, alléluia. Ps. Que Dieu se lève et que ses ennemis soient dispersés : et que ceux qui le haïssent fuient devant sa face."
        ),
        gradual = ProperChant(
            part = "All.",
            latinTitle = "Emitte Spiritum tuum",
            vernacularTitle = "Envoie ton Esprit",
            mode = "IV",
            gabc = """name: Emitte Spiritum;
office-part: Alleluia;
mode: 4;
book: Graduale Romanum, 1961, p. 295 & The Liber Usualis, 1961, p. 879;
transcriber: Andrew Hinkley;
commentary: Ps 103: 30;
%%
(c4) AL(fd~)le(e!fg)lú(gh)ia.(h.) *(;) <i>ij.</i>(ixhiHG/hff/ded) (::)
(z) <sp>V/</sp>. E(h)mít(gh)te(h) Spí(h)ri(g)tum(fe) tu(fg)um,(g.) (;) et(g) cre(fe)a(dc)bún(ef)tur :(d.) *(:) et(d) re(fe)no(fg)vá(g)bis(g) fá(gh)ci(f)em(fe) ter(de)rae.(e.) (::)""",
            translation = "Alléluia. ℣. Envoie ton Esprit, et ils seront créés, et tu renouvelleras la face de la terre."
        ),
        alleluiaOrTract = ProperChant(
            part = "All.",
            latinTitle = "Veni Sancte Spiritus",
            vernacularTitle = "Viens, Esprit Saint",
            mode = "II",
            gabc = """name: Veni Sancte Spiritus;
office-part: Alleluia;
mode: 2;
book: Graduale Romanum, 1961, p. 295 & The Liber Usualis, 1961, p. 880;
transcriber: Andrew Hinkley;
%%
(c4) AL(cd)le(f)lú(fg)ia.(f.) *(;) <i>ij.</i>(e/ghg/fe/ddc) (::)
(z) <sp>V/</sp>. Ve(f)ni(gh) Sanc(h)te(g) Spí(fe)ri(fg)tus,(g.) (;) re(f)ple(f) tu(f)ó(f)rum(f) cor(gh)da(g) fi(fe)dé(d)li(cd)um :(d.) *(:) et(f) tu(gh)i(h) a(h)mó(h)ris(g) in(f) e(fe)is(d) (;) i(ef)gnem(g) ac(fe)cén(cd)de.(d.) (::)""",
            translation = "Alléluia. ℣. Viens, Esprit Saint, remplis le cœur de tes fidèles, et allume en eux le feu de ton amour."
        ),
        sequence = ProperChant(
            part = "Seq.",
            latinTitle = "Veni Sancte Spiritus",
            vernacularTitle = "Séquence de la Pentecôte",
            mode = "I",
            gabc = """name: Veni Sancte Spiritus (Seq.);
office-part: Sequentia;
mode: 1;
book: Graduale Romanum, 1961, p. 296 & The Liber Usualis, 1961, p. 880;
transcriber: Andrew Hinkley;
%%
(c4) VE(c)ni(d) Sanc(e)te(f) Spí(ed)ri(c)tus,(d.) *(,) et(f) e(g)mít(h)te(ixi) caé(h)li(g)tus(h.) (;) lu(f)cis(g) tu(h)ae(g) rá(f)di(e)um.(d.) (::)
Ve(c)ni(d) pa(e)ter(f) páu(ed)pe(c)rum,(d.) (;) ve(f)ni(g) da(h)tor(ixi) mú(h)ne(g)rum,(h.) (;) ve(f)ni(g) lu(h)men(g) cór(f)di(e)um.(d.) (::)
Con(h)so(j)lá(k)tor(k) óp(ixi)ti(h)me,(h.) (;) dul(f)cis(g) hos(h)pes(g) á(f)ni(e)mae,(d.) (;) dul(f)ce(g) re(h)fri(g)gé(f)ri(e)um.(d.) (::)
Al(f)le(e)lú(d)ia.(d.) (::)""",
            translation = "Viens, Esprit Saint, et envoie du haut du ciel un rayon de ta lumière. Viens, père des pauvres ; viens, dispensateur des dons ; viens, lumière des cœurs."
        ),
        offertory = ProperChant(
            part = "Offert.",
            latinTitle = "Confirma hoc Deus",
            vernacularTitle = "Affermis, ô Dieu",
            mode = "IV",
            gabc = """name: Confirma hoc;
office-part: Offertorium;
mode: 4;
book: Graduale Romanum, 1961, p. 297 & The Liber Usualis, 1961, p. 882;
transcriber: Andrew Hinkley;
commentary: Ps 67: 29-30;
%%
(c4) CON(df)fír(f)ma(f) hoc(gh) De(gf)us,(e.) *(,) quod(f) o(g)pe(f)rá(fe)tus(d) es(c) in(df) no(e)bis :(d.) (:) a(f) tem(gh)plo(h) tu(h)o,(g.) (;) quod(fe) est(fg) in(f) Je(fe)rú(d)sa(cd)lem,(d.) (;) ti(f)bi(d) óf(f/gh)fe(g)rent(e.) (;) re(ef/gf)ges(fe) mú(d)ne(cd)ra,(d.) (;) al(f)le(fd)lú(f/gh)ia.(g.) (::)""",
            translation = "Affermis, ô Dieu, ce que tu as opéré en nous : depuis ton temple qui est dans Jérusalem, les rois t'offriront des présents, alléluia."
        ),
        communion = ProperChant(
            part = "Comm.",
            latinTitle = "Factus est repente",
            vernacularTitle = "Il se fit soudain du ciel un bruit",
            mode = "VII",
            gabc = """name: Factus est repente;
office-part: Communio;
mode: 7;
book: Graduale Romanum, 1961, p. 298 & The Liber Usualis, 1961, p. 882;
transcriber: Andrew Hinkley;
commentary: Act 2: 2, 4;
%%
(c3) FAC(e)tus(ei) est(i) re(i)pén(i)te(h) *(,) de(ij) cae(ih)lo(h) so(hi)nus,(h.) (:) ad(e)ve(f)ni(h)én(h)tis(h) spí(i)ri(h)tus(g) ve(f)he(g)mén(fe)tis,(e.) (;) u(f)bi(e) e(f)rant(h) se(i)dén(h)tes,(e.) (;) al(f)le(fh)lú(hg)ia :(fe) (:) et(e) re(f)plé(h)ti(i) sunt(h) om(ij)nes(i.) (;) Spí(i)ri(h)tu(f) Sanc(hi)to,(h.) (;) lo(e)quén(fh)tes(h) ma(hg)gná(fe)li(f)a(e) De(de)i,(e.) (;) al(e)le(fh)lú(hg)ia,(fe) (;) al(f)le(ef)lú(e.)ia.(e.) (::)""",
            translation = "Il se fit soudain du ciel un bruit comme celui d'un vent violent qui soufflait là où ils étaient assis, alléluia : et ils furent tous remplis du Saint-Esprit, proclamant les merveilles de Dieu, alléluia, alléluia."
        )
    )

    // 8. In Festo Sanctissimae Trinitatis (Sainte Trinité)
    val trinitySunday = ProperSet(
        id = "trinity_sunday",
        feastLatin = "In Festo Sanctissimae Trinitatis",
        feastVernacular = "Fête de la Très Sainte Trinité",
        season = LiturgicalSeason.TIME_AFTER_PENTECOST,
        introit = ProperChant(
            part = "Intr.",
            latinTitle = "Benedicta sit",
            vernacularTitle = "Bénie soit la Sainte Trinité",
            mode = "VIII",
            gabc = """name: Benedicta sit;
office-part: Introitus;
mode: 8;
book: Graduale Romanum, 1961, p. 308 & The Liber Usualis, 1961, p. 911;
transcriber: Andrew Hinkley;
commentary: Tob 12: 6; Ps 8: 2;
%%
(c4) BE(g)ne(g)díc(g)ta(g) sit(g) * sanc(h)ta(g) Trí(h)ni(j)tas,(i.) (;) at(j)que(i) in(h)di(g)ví(h)sa(g) U(f)ni(gh)tas :(h.) (:) con(f)fi(h)té(j)bi(i)mur(h) e(g)i,(g.) (;) qui(g)a(h) fe(j)cit(i) no(h)bís(g)cum(f.) (;) mi(h)se(j)ri(i)cór(h)di(g)am(fg) su(g.)am.(g.) (::)
(z) <sp>V/</sp>. Dó(g)mi(hj)ne(j) Dó(j)mi(j)nus(j) nos(k)ter :(j.) *(:) quam(ih) ad(hj)mi(j)rá(j)bi(j)le(j) est(j) no(j)men(j) tu(j)um(j) in(j) u(j)ni(j)vér(j)sa(j) ter(h)ra !(g.) (::)""",
            translation = "Bénie soit la Sainte Trinité et l'indivisible Unité : nous lui rendrons grâces, parce qu'elle a fait éclater sur nous sa miséricorde. Ps. Seigneur, notre Seigneur, que ton nom est admirable sur toute la terre !"
        ),
        gradual = ProperChant(
            part = "Grad.",
            latinTitle = "Benedictus es Domine",
            vernacularTitle = "Tu es béni, Seigneur",
            mode = "V",
            gabc = """name: Benedictus es Domine;
office-part: Graduale;
mode: 5;
book: Graduale Romanum, 1961, p. 309 & The Liber Usualis, 1961, p. 912;
transcriber: Andrew Hinkley;
commentary: Dan 3: 55-56;
%%
(c4) BE(d)ne(ff)díc(g)tus(f) es(fe) Dó(d)mi(c)ne,(d.) *(;) qui(f) in(f)tu(gh)é(h)ris(g) a(fe)býs(fg)sos,(f.) (;) et(f) se(f)des(gh) su(h)per(g) Ché(fe)ru(fg)bim.(f.) (::)
(z) <sp>V/</sp>. Be(f)ne(gh)díc(h)tus(g) es(f) Dó(gh)mi(h)ne(h.) (;) in(h) fir(h)ma(h)mén(g)to(fe) cae(fg)li,(f.) *(:) et(f) lau(gh)dá(h)bi(g)lis(f) in(ef) saé(g)cu(fe)la.(cd/ed) (::)""",
            translation = "Tu es béni, Seigneur, toi qui regardes les abîmes et es assis sur les Chérubins. ℣. Tu es béni, Seigneur, dans le firmament du ciel, et louable pour les siècles."
        ),
        alleluiaOrTract = ProperChant(
            part = "All.",
            latinTitle = "Benedictus es Domine Deus",
            vernacularTitle = "Béni sois-tu, Seigneur Dieu de nos pères",
            mode = "VIII",
            gabc = """name: Benedictus es Domine Deus;
office-part: Alleluia;
mode: 8;
book: Graduale Romanum, 1961, p. 310 & The Liber Usualis, 1961, p. 912;
transcriber: Andrew Hinkley;
commentary: Dan 3: 52;
%%
(c4) AL(g)le(gg)lú(h)ia.(ghg.) *(;) <i>ij.</i>(hvGF/ghg) (::)
(z) <sp>V/</sp>. Be(g)ne(f)díc(gh)tus(h) es(g) Dó(gh)mi(g)ne(g.) (;) De(g)us(g) pa(h)trum(j) nos(i)tró(h)rum,(g.) *(:) et(f) lau(h)dá(j)bi(i)lis(h) in(g) saé(fe)cu(fg)la.(g.) (::)""",
            translation = "Alléluia. ℣. Tu es béni, Seigneur Dieu de nos pères, et digne de louange dans tous les siècles."
        ),
        offertory = ProperChant(
            part = "Offert.",
            latinTitle = "Benedictus sit Deus",
            vernacularTitle = "Béni soit Dieu le Père",
            mode = "III",
            gabc = """name: Benedictus sit Deus;
office-part: Offertorium;
mode: 3;
book: Graduale Romanum, 1961, p. 310 & The Liber Usualis, 1961, p. 913;
transcriber: Andrew Hinkley;
commentary: Tob 12: 6;
%%
(c4) BE(e)ne(e)díc(e)tus(e) sit(e) * De(e)us(e) Pa(fe)ter,(g.) (;) u(g)ni(g)ge(g)ni(g)tús(gh)que(g) De(f)i(e) Fí(fg)li(f)us,(e.) (:) Sanc(g)tus(gh) quo(h)que(g) Spí(fe)ri(de)tus.(e.) (::)""",
            translation = "Béni soit Dieu le Père, ainsi que le Fils unique de Dieu, et le Saint-Esprit."
        ),
        communion = ProperChant(
            part = "Comm.",
            latinTitle = "Benedicimus Deum caeli",
            vernacularTitle = "Bénissons le Dieu du ciel",
            mode = "IV",
            gabc = """name: Benedicimus Deum caeli;
office-part: Communio;
mode: 4;
book: Graduale Romanum, 1961, p. 311 & The Liber Usualis, 1961, p. 914;
transcriber: Andrew Hinkley;
commentary: Tob 12: 6;
%%
(c4) BE(e)ne(e)dí(f)ci(e)mus(e.) * De(e)um(f) cae(gh)li,(g.) (:) et(g) co(f)ram(g) óm(h)ni(g)bus(f) vi(g)vén(fe)ti(d)bus(c.) (;) con(d)fi(f)té(g)bi(e)mur(fe) e(d)i.(e.) (::)""",
            translation = "Bénissons le Dieu du ciel, et confessons sa grandeur devant tous les vivants."
        )
    )

    // 9. In Festo Sanctissimi Corporis Christi (Fête-Dieu)
    val corpusChristi = ProperSet(
        id = "corpus_christi",
        feastLatin = "In Festo Sanctissimi Corporis Christi",
        feastVernacular = "Fête du Très Saint Sacrement (Fête-Dieu)",
        season = LiturgicalSeason.TIME_AFTER_PENTECOST,
        introit = ProperChant(
            part = "Intr.",
            latinTitle = "Cibavit eos",
            vernacularTitle = "Il les a nourris de la fleur du froment",
            mode = "II",
            gabc = """name: Cibavit eos (cum Alleluia);
office-part: Introitus;
mode: 2;
book: Graduale Romanum, 1961, p. 312 & The Liber Usualis, 1961, p. 917;
transcriber: Andrew Hinkley;
commentary: Ps 80: 17, 2;
%%
(c3) CI(c)bá(efe)vit(e) e(f)os(ed) *(,) ex(d) á(ef)di(f)pe(f) fru(f)mén(ef)ti,(f.) (;) al(e)le(f)lú(h)ia :(h.) (:) et(h) de(gh) pe(f)tra,(ed) mel(ef)le(f.) (;) sa(f)tu(f)rá(ef)vit(f) e(e!fh)os,(h.) (;) al(hh)le(fe)lú(f)ia,(e.) (;) al(f)le(ef)lú(e.)ia.(e.) (::)
(z) <sp>V/</sp>. Ex(e)sul(f)tá(h)te(h) De(h)o(h) ad(h)ju(h)tó(h)ri(i) nos(h)tro :(h.) *(:) ju(h)bi(h)lá(h)te(h) De(h)o(g) Ja(f)cob.(gh) (::)""",
            translation = "Il les a nourris de la fleur du froment, alléluia : et il les a rassasiés du miel du rocher, alléluia, alléluia. Ps. Tressaillez d'allégresse en Dieu notre protecteur : poussez des cris de joie vers le Dieu de Jacob."
        ),
        gradual = ProperChant(
            part = "Grad.",
            latinTitle = "Oculi omnium",
            vernacularTitle = "Les yeux de tous",
            mode = "VII",
            gabc = """name: Oculi omnium;
office-part: Graduale;
mode: 7;
book: Graduale Romanum, 1961, p. 313 & The Liber Usualis, 1961, p. 918;
transcriber: Andrew Hinkley;
commentary: Ps 144: 15-16;
%%
(c3) O(ef)cu(e)li(e) óm(e)ni(e)um(f.) *(,) in(e) te(f) spe(h)rant(h) Dó(i)mi(h)ne :(h.) (:) et(h) tu(h) das(h) e(h)scam(i) il(g)ló(fe)rum(f.) (;) in(e) tém(f)po(g)re(f) op(e)por(ef)tú(e)no.(e.) (::)
(z) <sp>V/</sp>. A(e)pe(ef)ris(e) tu(e) ma(f)num(h) tu(h)am :(h.) *(:) et(h) im(h)ples(h) om(h)ne(i) á(h)ni(g)mal(fe) (;) be(f)ne(e)dic(f)ti(g)ó(f)ne.(e.) (::)""",
            translation = "Les yeux de tous espèrent en toi, Seigneur : et tu leur donnes la nourriture en temps opportun. ℣. Tu ouvres ta main, et tu combles de bénédictions tout être vivant."
        ),
        alleluiaOrTract = ProperChant(
            part = "All.",
            latinTitle = "Caro mea vere est cibus",
            vernacularTitle = "Ma chair est vraiment une nourriture",
            mode = "VII",
            gabc = """name: Caro mea;
office-part: Alleluia;
mode: 7;
book: Graduale Romanum, 1961, p. 314 & The Liber Usualis, 1961, p. 919;
transcriber: Andrew Hinkley;
commentary: Joann 6: 56-57;
%%
(c3) AL(e)le(ef)lú(h)ia.(h.) *(;) <i>ij.</i>(hg/hih) (::)
(z) <sp>V/</sp>. Ca(ef)ro(h) me(h)a(h) ve(h)re(h) est(h) ci(g)bus,(f.) (;) et(e) san(f)guis(h) me(h)us(h) ve(h)re(g) est(f) po(g!hi)tus :(i.) *(:) qui(i) man(i)dú(h)cat(g) me(f)am(e) car(fe)nem,(e.) (;) in(e) me(f) ma(g)net(f) et(e) e(ef)go(e) in(e) e(e)o.(e.) (::)""",
            translation = "Alléluia. ℣. Ma chair est vraiment une nourriture, et mon sang est vraiment un breuvage : celui qui mange ma chair et boit mon sang demeure en moi et moi en lui."
        ),
        sequence = ProperChant(
            part = "Seq.",
            latinTitle = "Lauda Sion Salvatorem",
            vernacularTitle = "Loue, ô Sion, ton Sauveur",
            mode = "VII",
            gabc = """name: Lauda Sion;
office-part: Sequentia;
mode: 7;
book: Graduale Romanum, 1961, p. 314 & The Liber Usualis, 1961, p. 920;
transcriber: Andrew Hinkley;
%%
(c3) LAu(e)da(f) Si(e)on(d) Sal(e)va(f)tó(h)rem,(h.) *(,) lau(i)da(h) du(g)cem(f) et(g) pas(h)tó(gvFE)rem,(e.) (;) in(h) hym(f)nis(d) et(e) cán(f)ti(e)cis.(e.) (::)
Quan(e)tum(f) po(e)tes,(d) tan(e)tum(f) au(h)de :(h.) (;) qui(i)a(h) ma(g)jor(f) om(g)ni(h) lau(gvFE)de,(e.) (;) nec(h) lau(f)dá(d)re(e) súf(f)fi(e)cis.(e.) (::)
Ec(h)ce(k) pa(j)nis(i) An(h)ge(g)ló(h)rum,(h.) (;) fac(e)tus(f) ci(h)bus(g) vi(f)a(e)tó(d)rum :(e.) (;) ve(h)re(f) pa(d)nis(e) fi(f)li(e)ó(d)rum.(e.) (::)
Al(e)le(f)lú(e)ia.(e.) (::)""",
            translation = "Loue, ô Sion, ton Sauveur, loue ton guide et ton pasteur par des hymnes et des cantiques. Ose autant que tu peux, car il surpasse toute louange !"
        ),
        offertory = ProperChant(
            part = "Offert.",
            latinTitle = "Sacerdotes Domini",
            vernacularTitle = "Les prêtres du Seigneur",
            mode = "IV",
            gabc = """name: Sacerdotes Domini;
office-part: Offertorium;
mode: 4;
book: Graduale Romanum, 1961, p. 318 & The Liber Usualis, 1961, p. 924;
transcriber: Andrew Hinkley;
commentary: Lev 21: 6;
%%
(c4) SA(df)cer(f)dó(gh)tes(g) Dó(gf)mi(ef)ni(e.) *(,) in(f)cén(gh)sum(h) et(g) pa(fe)nes(f) óf(fe)fe(d)runt(c) De(df)o :(d.) (:) et(f) í(gh)de(h)o(h.) (;) sanc(ixhi)ti(h) e(g)runt(f) De(ef)o(g) su(fe)o,(d.) (;) al(f)le(fd)lú(f/gh)ia.(g.) (::)""",
            translation = "Les prêtres du Seigneur offrent à Dieu l'encens et les pains : c'est pourquoi ils seront saints pour leur Dieu, alléluia."
        ),
        communion = ProperChant(
            part = "Comm.",
            latinTitle = "Quotiescumque manducabitis",
            vernacularTitle = "Toutes les fois que vous mangerez ce pain",
            mode = "VII",
            gabc = """name: Quotiescumque;
office-part: Communio;
mode: 7;
book: Graduale Romanum, 1961, p. 319 & The Liber Usualis, 1961, p. 925;
transcriber: Andrew Hinkley;
commentary: 1 Cor 11: 26-27;
%%
(c3) QUo(e)ti(f)es(h)cúm(h)que(h) * man(h)du(h)cá(i)bi(h)tis(g) pa(fe)nem(f) hunc,(e.) (:) mor(f)tem(h) Dó(i)mi(h)ni(h) an(h)nun(h)ti(h)á(i)bi(h)tis,(g) do(fe)nec(f) vé(e)ni(ef)at.(e.) (::)""",
            translation = "Toutes les fois que vous mangerez ce pain et boirez ce calice, vous annoncerez la mort du Seigneur jusqu'à ce qu'il vienne."
        )
    )

    // 10. In Festo Domini Nostri Jesu Christi Regis (Christ-Roi - Dernier Dimanche d'Octobre)
    val christTheKing = ProperSet(
        id = "christ_the_king",
        feastLatin = "In Festo D. N. Jesu Christi Regis",
        feastVernacular = "Fête de Notre-Seigneur Jésus-Christ Roi",
        season = LiturgicalSeason.TIME_AFTER_PENTECOST,
        introit = ProperChant(
            part = "Intr.",
            latinTitle = "Dignus est Agnus",
            vernacularTitle = "Digne est l'Agneau",
            mode = "III",
            gabc = """name: Dignus est Agnus;
office-part: Introitus;
mode: 3;
book: Graduale Romanum, 1961, p. 642 & The Liber Usualis, 1961, p. 1709;
transcriber: Andrew Hinkley;
commentary: Apoc 5: 12; 1: 6; Ps 71: 1;
%%
(c4) DI(e!fg)gnus(g) est(gh) A(gvFE)gnus,(e.) *(,) qui(e) oc(g)cí(h)sus(i) est,(i.) (;) ac(i)cí(j)pe(i)re(h) vir(g)tú(h)tem,(h.) (;) et(g) di(i)vi(j)ni(h)tá(ij)tem,(g.) (;) et(g) sa(h)pi(g)én(fe~)ti(de)am,(e.) (:) et(e) for(e)ti(e)tú(fe)di(de)nem,(e.) (;) et(e) ho(fg)nó(gvFE)rem.(e.) (:) Ip(i)si(h) gló(j)ri(i)a(h) et(g) im(h)pé(g)ri(f)um(e.) (;) in(g) saé(h)cu(g)la(f) sae(e)cu(f)ló(g)rum.(e.) (::)
(z) <sp>V/</sp>. De(g)us(hj) ju(j)dí(j)ci(j)um(j) tu(j)um(j) Re(k)gi(j) da :(ih) *(:) et(hj) jus(j)tí(j)ti(j)am(j) tu(j)am(j) Fí(j)li(i)o(g) Re(h)gis.(i.) (::)""",
            translation = "Digne est l'Agneau qui a été immolé, de recevoir la puissance, la divinité, la sagesse, la force et l'honneur. À lui la gloire et l'empire dans les siècles des siècles. Ps. Ô Dieu, donne tes jugements au Roi, et ta justice au Fils du Roi."
        ),
        gradual = ProperChant(
            part = "Grad.",
            latinTitle = "Dominabitur",
            vernacularTitle = "Il dominera d'une mer à l'autre",
            mode = "V",
            gabc = """name: Dominabitur;
office-part: Graduale;
mode: 5;
book: Graduale Romanum, 1961, p. 643 & The Liber Usualis, 1961, p. 1710;
transcriber: Andrew Hinkley;
commentary: Ps 71: 8, 11;
%%
(c4) DO(d)mi(ff)ná(g)bi(f)tur(f.) *(;) a(f) ma(gh)ri(h) us(g)que(f) ad(gh) ma(g)re,(f.) (:) et(f) a(f) flú(gh)mi(h)ne(h.) (;) us(h)que(g) ad(f) ter(fe)mi(d)nos(c) or(ef)bis(g) ter(fe)rá(cd)rum.(d.) (::)
(z) <sp>V/</sp>. Et(f) ad(f)o(gh)rá(h)bunt(h) e(h)um(h.) om(h)nes(g) re(f)ges(gh) ter(g)rae :(f.) *(:) om(f)nes(fe) gen(d)tes(c) sér(ef)vi(g)ent(fe) e(cd)i.(d.) (::)""",
            translation = "Il dominera d'une mer à l'autre mer, et depuis le fleuve jusqu'aux extrémités de la terre. ℣. Et tous les rois de la terre l'adoreront : toutes les nations le serviront."
        ),
        alleluiaOrTract = ProperChant(
            part = "All.",
            latinTitle = "Potestas ejus",
            vernacularTitle = "Sa puissance est une puissance éternelle",
            mode = "V",
            gabc = """name: Potestas ejus;
office-part: Alleluia;
mode: 5;
book: Graduale Romanum, 1961, p. 644 & The Liber Usualis, 1961, p. 1711;
transcriber: Andrew Hinkley;
commentary: Dan 7: 14;
%%
(c4) AL(d)le(ffg)lú(f)ia.(f.) *(;) <i>ij.</i>(ghGF/efd) (::)
(z) <sp>V/</sp>. Po(f)tés(gh)tas(h) e(ixhi)jus,(h.) (;) po(h)tés(h)tas(g) ae(f)tér(gh)na,(g.) *(:) quae(f) non(fe) au(d)fe(cd)ré(ef)tur :(f.) (;) et(f) re(gh)gnum(h) e(h)jus,(h.) (;) quod(ixhi) non(gf) cor(ef)rum(g)pé(fe)tur.(cd) (::)""",
            translation = "Alléluia. ℣. Sa puissance est une puissance éternelle, qui ne lui sera jamais enlevée ; et son royaume ne sera jamais détruit."
        ),
        offertory = ProperChant(
            part = "Offert.",
            latinTitle = "Postula a me",
            vernacularTitle = "Demande-moi et je te donnerai",
            mode = "IV",
            gabc = """name: Postula a me;
office-part: Offertorium;
mode: 4;
book: Graduale Romanum, 1961, p. 644 & The Liber Usualis, 1961, p. 1712;
transcriber: Andrew Hinkley;
commentary: Ps 2: 8;
%%
(c4) PÓS(df)TU(f)LA(f) a(gh) me,(e.) *(,) et(f) da(g)bo(f) ti(fe)bi(d) gen(c)tes(df) he(e)re(f)di(d)tá(e)tem(c) tu(de)am,(d.) (:) et(f) pos(gh)ses(h)si(g)ó(fe)nem(f) tu(g)am(f.) (;) tér(fe)mi(d)nos(c) ter(de)rae.(e.) (::)""",
            translation = "Demande-moi, et je te donnerai les nations pour héritage, et les extrémités de la terre pour ton domaine."
        ),
        communion = ProperChant(
            part = "Comm.",
            latinTitle = "Sedebit Dominus Rex",
            vernacularTitle = "Le Seigneur siégera en Roi pour l'éternité",
            mode = "VI",
            gabc = """name: Sedebit Dominus Rex;
office-part: Communio;
mode: 6;
book: Graduale Romanum, 1961, p. 645 & The Liber Usualis, 1961, p. 1713;
transcriber: Andrew Hinkley;
commentary: Ps 28: 10, 11;
%%
(c4) SE(f)dé(f)bit(e) Dó(g)mi(f)nus(e) Rex(d.) *(,) in(f) ae(fe)tér(d)num :(c.) (:) Dó(f)mi(f)nus(fe) be(d)ne(e)dí(f)cet(f.) (;) pó(f)pu(fe)lo(d) su(e)o(f) in(g) pa(fe)ce.(d.) (::)""",
            translation = "Le Seigneur siégera en Roi pour toujours : le Seigneur bénira son peuple dans la paix."
        )
    )

    // 11. In Festo Omnium Sanctorum (Toussaint - 1er Novembre)
    val allSaints = ProperSet(
        id = "all_saints",
        feastLatin = "In Festo Omnium Sanctorum",
        feastVernacular = "Fête de Tous les Saints (Toussaint)",
        season = LiturgicalSeason.TIME_AFTER_PENTECOST,
        introit = ProperChant(
            part = "Intr.",
            latinTitle = "Gaudeamus omnes",
            vernacularTitle = "Réjouissons-nous tous dans le Seigneur",
            mode = "I",
            gabc = """name: Gaudeamus... Sanctorum omnium;
office-part: Introitus;
mode: 1;
book: Graduale Romanum, 1961, p. 651 & The Liber Usualis, 1961, p. 1725;
transcriber: Andrew Hinkley;
commentary: Ps 32: 1;
%%
(c4) GAu(c)de(d)á(ixdh/iv)mus(h.) *(,) om(h)nes(g) in(h) Dó(hk)mi(j)no,(ikjj) (;) di(h)em(g) fes(h)tum(g) ce(f)le(g)brán(h)tes(h.) (;) sub(h) ho(g)nó(f)re(e) Sanc(g)tó(ixhi)rum(h) óm(g)ni(fg)um :(f.) (:) de(f) quo(g)rum(h) so(g)lem(f)ni(e)tá(fg)te(g.) (;) gau(e!fg)dent(f) An(fg)ge(f)li,(fED.) (;) et(c) col(d)láu(f)dant(fg) Fí(g)li(f)um(fe) De(cd)i.(d.) (::)
(z) <sp>V/</sp>. Ex(f)sul(gh)tá(h)te(h) jus(h)ti(h) in(h) Dó(ixhi)mi(h)no :(h.) *(:) rec(h)tos(h) de(h)cet(h) col(h)lau(g)dá(f)ti(gh)o.(h.) (::)""",
            translation = "Réjouissons-nous tous dans le Seigneur, célébrant ce jour de fête en l'honneur de tous les Saints : de leur solennité les Anges se réjouissent et ils louent le Fils de Dieu. Ps. Justes, réjouissez-vous dans le Seigneur : la louange convient aux hommes droits."
        ),
        gradual = ProperChant(
            part = "Grad.",
            latinTitle = "Timete Dominum",
            vernacularTitle = "Craignez le Seigneur, vous tous ses saints",
            mode = "I",
            gabc = """name: Timete Dominum;
office-part: Graduale;
mode: 1;
book: Graduale Romanum, 1961, p. 652 & The Liber Usualis, 1961, p. 1726;
transcriber: Andrew Hinkley;
commentary: Ps 33: 10, 11;
%%
(c4) TI(c)mé(d)te(f) Dó(fe)mi(d)num(d.) *(;) om(f)nes(gh) sanc(ixhi)ti(h) e(g)jus :(fe..) (:) quó(f)ni(e)am(d) ni(fg)hil(g) de(f)est(e.) (;) ti(d)mén(e)ti(f)bus(e) e(d.)um.(d.) (::)
(z) <sp>V/</sp>. In(h)qui(h)rén(ixhi)tes(h) au(g)tem(f) Dó(gh)mi(g)num,(fe..) *(:) non(d) mi(fe)nu(fg)én(gf)tur(d.) (;) om(c)ni(df) bo(e)no.(d.) (::)""",
            translation = "Craignez le Seigneur, vous tous ses saints, car rien ne manque à ceux qui le craignent. ℣. Mais ceux qui cherchent le Seigneur ne seront privés d'aucun bien."
        ),
        alleluiaOrTract = ProperChant(
            part = "All.",
            latinTitle = "Venite ad me",
            vernacularTitle = "Venez à moi",
            mode = "VIII",
            gabc = """name: Venite ad me;
office-part: Alleluia;
mode: 8;
book: Graduale Romanum, 1961, p. 653 & The Liber Usualis, 1961, p. 1727;
transcriber: Andrew Hinkley;
commentary: Mt 11: 28;
%%
(c4) AL(g)le(gg)lú(h)ia.(ghg.) *(;) <i>ij.</i>(hvGF/ghg) (::)
(z) <sp>V/</sp>. Ve(g)ní(gh)te(h) ad(gh) me(g.) (;) om(h)nes(j) qui(i) la(j)bo(k)rá(ji)tis,(h.) (;) et(g) o(h)ne(j)rá(i)ti(g) es(h)tis :(g.) *(:) et(g) e(h)go(j) re(k)fí(j)ci(ij)am(h) vos.(ghg.) (::)""",
            translation = "Alléluia. ℣. Venez à moi, vous tous qui ployez sous le fardeau et peinez, et je vous procurerai le repos."
        ),
        offertory = ProperChant(
            part = "Offert.",
            latinTitle = "Justorum animae",
            vernacularTitle = "Les âmes des justes sont dans la main de Dieu",
            mode = "I",
            gabc = """name: Justorum animae;
office-part: Offertorium;
mode: 1;
book: Graduale Romanum, 1961, p. 653 & The Liber Usualis, 1961, p. 1727;
transcriber: Andrew Hinkley;
commentary: Sap 3: 1, 2, 3;
%%
(c4) JUS(f)tó(gh)rum(h) á(h)ni(g)mae(f.) *(,) in(f) ma(gh)nu(h) De(ixhi)i(h) sunt,(h.) (:) et(h) non(g) tan(f)get(fe) il(d)los(c) tor(ef)mén(g)tum(f) ma(fe)lí(d)ti(cd)ae :(d.) (:) vi(f)si(gh) sunt(h) ó(ixhi)cu(h)lis(h) in(g)si(f)pi(gh)én(g)ti(f)um(fe) mo(d)ri :(cd) (:) il(d)li(fe) au(fg)tem(g) sunt(fe) in(cd) pa(e)ce.(d.) (::)""",
            translation = "Les âmes des justes sont dans la main de Dieu, et le tourment de la malice ne les touchera point. Ils ont paru mourir aux yeux des insensés : mais ils sont dans la paix."
        ),
        communion = ProperChant(
            part = "Comm.",
            latinTitle = "Beati mundo corde",
            vernacularTitle = "Bienheureux ceux qui ont le cœur pur",
            mode = "I",
            gabc = """name: Beati mundo corde;
office-part: Communio;
mode: 1;
book: Graduale Romanum, 1961, p. 654 & The Liber Usualis, 1961, p. 1728;
transcriber: Andrew Hinkley;
commentary: Mt 5: 8, 9, 10;
%%
(c4) BE(d)á(f)ti(fe) mun(d)do(c) cor(d)de,(d.) *(,) quó(f)ni(g)am(h) ip(g)si(f) De(fe)um(g) vi(fe)dé(d)bunt :(d.) (:) be(f)á(gh)ti(h) pa(g)cí(h)fi(g)ci,(f.) (;) quó(f)ni(g)am(h) fí(j)li(i)i(h) De(g)i(h) vo(fe)ca(g)bún(f)tur.(d.) (::)""",
            translation = "Bienheureux ceux qui ont le cœur pur, car ils verront Dieu : bienheureux les artisans de paix, car ils seront appelés enfants de Dieu."
        )
    )

    // 12. In Conceptione Immaculata Beatae Mariae Virginis (8 Décembre)
    val immaculateConception = ProperSet(
        id = "immaculate_conception",
        feastLatin = "In Conceptione Immaculata Beatae Mariae Virginis",
        feastVernacular = "Immaculée Conception de la B.V. Marie (8 Décembre)",
        season = LiturgicalSeason.ADVENT,
        introit = ProperChant(
            part = "Intr.",
            latinTitle = "Gaudens gaudebo",
            vernacularTitle = "Je tressaille de joie dans le Seigneur",
            mode = "III",
            gabc = """name: Gaudens gaudebo;
office-part: Introitus;
mode: 3;
book: Graduale Romanum, 1961, p. [1] & The Liber Usualis, 1961, p. 1261;
transcriber: Andrew Hinkley;
commentary: Is 61: 10; Ps 29: 2;
%%
(c4) GAu(e)dens(ed) gau(g)dé(hj)bo(j.) *(,) in(ij) Dó(h)mi(g)no,(g.) (;) et(g) ex(h)sul(j)tá(j)bit(i) á(h)ni(g)ma(f) me(g)a(h) in(h) De(g)o(h) me(i)o :(g.) (:) qui(h)a(j) índ(j)u(j)it(i) me(h) ves(i)ti(j)mén(h)tis(g) sa(h)lú(g)tis,(f.) (;) et(f) in(g)du(h)mén(h)to(g) jus(h)tí(i)ti(h)ae(g) cir(f)cúm(g)de(h)dit(g) me.(e.) (::)
(z) <sp>V/</sp>. Ex(g)al(hj)tá(j)bo(j) te,(j) Dó(j)mi(j)ne,(j) quó(j)ni(j)am(j) sus(j)ce(k)pís(j)ti(j) me :(ih) *(:) nec(hj) de(j)lec(j)tás(j)ti(j) in(j)i(j)mí(j)cos(i) me(g)os(h) su(i)per(g) me.(e.) (::)""",
            translation = "Je tressaillirai d'allégresse dans le Seigneur, et mon âme exultera en mon Dieu : car il m'a revêtue des vêtements du salut. Ps. Je t'exalterai, Seigneur, parce que tu m'as accueilli."
        ),
        gradual = ProperChant(
            part = "Grad.",
            latinTitle = "Benedicta es tu",
            vernacularTitle = "Tu es bénie, Vierge Marie",
            mode = "V",
            gabc = """name: Benedicta es tu;
office-part: Graduale;
mode: 5;
book: Graduale Romanum, 1961, p. [2] & The Liber Usualis, 1961, p. 1262;
transcriber: Andrew Hinkley;
commentary: Judith 13: 23;
%%
(c4) BE(d)ne(ff)díc(g)ta(f) es(fe) tu,(d.) *(;) Vir(f)go(gh) Ma(h)rí(gf)a,(f.) (;) a(f) Dó(gh)mi(g)no(fe) De(cd)o(d.) (;) ex(fg)cél(f)so(e) su(f)per(g) om(fe)nes(cd) mu(e)lí(d)e(c)res.(d.) (::)""",
            translation = "Tu es bénie, Vierge Marie, par le Seigneur Dieu Très-Haut, par-dessus toutes les femmes de la terre."
        ),
        alleluiaOrTract = ProperChant(
            part = "All.",
            latinTitle = "Tota pulchra es",
            vernacularTitle = "Tu es toute belle, ô Marie",
            mode = "I",
            gabc = """name: Tota pulchra es;
office-part: Alleluia;
mode: 1;
book: Graduale Romanum, 1961, p. [3] & The Liber Usualis, 1961, p. 1263;
transcriber: Andrew Hinkley;
commentary: Cant 4: 7;
%%
(c4) AL(c)le(d)lú(ixdh/iv)ia.(h.) *(;) <i>ij.</i>(hvGF/ed/efe/ddc) (::)
(z) <sp>V/</sp>. To(h)ta(h) pul(h)chra(g) es,(fe) Ma(fg)rí(g)a :(d.) *(:) et(d) má(fe)cu(fg)la(g) o(fe)ri(d)gi(c)ná(ef)lis(g) non(fe) est(cd) in(e) te.(d.) (::)""",
            translation = "Alléluia. ℣. Tu es toute belle, ô Marie, et la tache originelle n'est point en toi."
        ),
        offertory = ProperChant(
            part = "Offert.",
            latinTitle = "Ave Maria",
            vernacularTitle = "Je vous salue Marie",
            mode = "VIII",
            gabc = """name: Ave Maria;
office-part: Offertorium;
mode: 8;
book: Graduale Romanum, 1961, p. [4] & The Liber Usualis, 1961, p. 1264;
transcriber: Andrew Hinkley;
commentary: Lc 1: 28, 42;
%%
(c4) A(g)ve(fe) Ma(fg)rí(g)a,(g.) *(,) grá(g)ti(h)a(j) ple(h)na ;(g.) (;) Dó(g!jj)mi(h)nus(gf) te(gh)cum :(h.) (:) be(g)ne(fe)díc(fg)ta(g) tu(g.) (;) in(hj) mu(h)li(gf)é(gh)ri(h)bus,(g.) (;) et(g) be(fe)ne(fg)díc(g)tus(g) fruc(hj)tus(h) ven(gf)tris(gh) tu(h)i.(g.) (::)""",
            translation = "Je vous salue Marie, pleine de grâce, le Seigneur est avec vous ; vous êtes bénie entre toutes les femmes, et le fruit de vos entrailles est béni."
        ),
        communion = ProperChant(
            part = "Comm.",
            latinTitle = "Gloriosa dicta sunt",
            vernacularTitle = "De glorieuses choses ont été dites",
            mode = "VIII",
            gabc = """name: Gloriosa dicta sunt;
office-part: Communio;
mode: 8;
book: Graduale Romanum, 1961, p. [5] & The Liber Usualis, 1961, p. 1264;
transcriber: Andrew Hinkley;
commentary: Ps 86: 3; Lc 1: 49;
%%
(c4) GLo(g)ri(g)ó(hj)sa(j) dic(j)ta(i) sunt(h) de(j) te,(g.) *(;) Ma(h)rí(gf)a :(e.) (:) qui(h)a(j) fe(i)cit(h) ti(j)bi(g) ma(h)gna(g.) (;) qui(gf) pot(e)ens(ef) est.(d.) (::)""",
            translation = "De glorieuses choses ont été dites de toi, Marie : car le Puissant a fait pour toi de grandes merveilles."
        )
    )

    // 13. Missa Defunctorum (Messe de Requiem)
    val requiemMass = ProperSet(
        id = "requiem_mass",
        feastLatin = "Missa Defunctorum (Requiem)",
        feastVernacular = "Messe des Défunts (Requiem)",
        season = LiturgicalSeason.TIME_AFTER_PENTECOST,
        introit = ProperChant(
            part = "Intr.",
            latinTitle = "Requiem aeternam",
            vernacularTitle = "Donne-leur le repos éternel",
            mode = "VI",
            gabc = """name: Requiem aeternam;
office-part: Introitus;
mode: 6;
book: Graduale Romanum, 1961, p. 94* & The Liber Usualis, 1961, p. 1807;
transcriber: Andrew Hinkley;
commentary: Ps 64: 2-3;
%%
(c4) RE(f)qui(fe)em(d) ae(f)tér(fg)nam(g.) *(,) do(h)na(g) e(fe)is(d) Dó(f)mi(g)ne :(f.) (:) et(f) lux(gh) per(h)pé(g)tu(f)a(fg) (;) lú(g)ce(fe)at(d) e(f!gh)is.(f.) (::)
(z) <sp>V/</sp>. Te(fg) de(g)cet(g) hym(g)nus(g) De(g)us(g) in(g) Si(h)on,(g.) *(:) et(f) ti(gh)bi(h) red(h)dé(h)tur(h) vo(h)tum(g) in(f) Je(gh)rú(g)sa(f)lem.(f.) (::)""",
            translation = "Donne-leur le repos éternel, Seigneur : et que la lumière sans déclin brille sur eux. Ps. À toi la louange est due, ô Dieu, en Sion, et à toi les vœux seront accomplis dans Jérusalem."
        ),
        gradual = ProperChant(
            part = "Grad.",
            latinTitle = "Requiem aeternam",
            vernacularTitle = "Le repos éternel",
            mode = "II",
            gabc = """name: Requiem aeternam;
office-part: Graduale;
mode: 2;
book: Graduale Romanum, 1961, p. 95* & The Liber Usualis, 1961, p. 1808;
transcriber: Andrew Hinkley;
commentary: 4 Esdr 2: 34, 35; Ps 111: 7;
%%
(c4) RE(f)qui(fe)em(d.) ae(f)tér(fg)nam(g.) *(;) do(h)na(g) e(fe)is(d) Dó(f)mi(g)ne :(f.) (:) et(f) lux(gh) per(h)pé(g)tu(f)a(fg) (;) lú(g)ce(fe)at(d) e(f!gh)is.(f.) (::)
(z) <sp>V/</sp>. In(f) me(gh)mó(h)ri(h)a(h) ae(h)tér(h)na(g.) *(:) e(h)rit(gf) jus(fg)tus :(f.) (:) ab(f) au(f)di(fe)ti(d)ó(e)ne(f) ma(g)la(f.) (;) non(fe) ti(cd)mé(e)bit.(d.) (::)""",
            translation = "Donne-leur le repos éternel, Seigneur : et que la lumière sans déclin brille sur eux. ℣. Le juste sera dans un souvenir éternel : il ne craindra pas les bruits malveillants."
        ),
        alleluiaOrTract = ProperChant(
            part = "Tract.",
            latinTitle = "Absolve Domine",
            vernacularTitle = "Absous, Seigneur",
            mode = "VIII",
            gabc = """name: Absolve Domine;
office-part: Tractus;
mode: 8;
book: Graduale Romanum, 1961, p. 96* & The Liber Usualis, 1961, p. 1809;
transcriber: Andrew Hinkley;
%%
(c4) AB(g)sól(gj)ve(j) Dó(j)mi(i)ne(h.) *(;) á(j)ni(i)mas(h) óm(g)ni(f)um(g) fi(h)dé(j)li(i)um(h) de(g)func(h)tó(g)rum(f.) (:) ab(h) om(j)ni(i) vín(h)cu(g)lo(h) de(f)lic(g)tó(h)rum.(g.) (::)""",
            translation = "Absous, Seigneur, les âmes de tous les fidèles défunts de tout lien de leurs péchés."
        ),
        sequence = ProperChant(
            part = "Seq.",
            latinTitle = "Dies irae",
            vernacularTitle = "Jour de colère",
            mode = "I",
            gabc = """name: Dies irae;
office-part: Sequentia;
mode: 1;
book: Graduale Romanum, 1961, p. 96* & The Liber Usualis, 1961, p. 1810;
transcriber: Andrew Hinkley;
%%
(c4) DI(f)es(e) i(f)rae,(d) di(e)es(c) il(d)la,(d.) *(,) Sol(f)vet(g) saec(h)lum(g) in(f) fa(g)víl(h)la :(h.) (;) Tes(h)te(j) Da(k)vid(h) cum(g) Si(f)býl(gh)la.(h.) (::)
Quan(h)tus(j) tre(k)mor(h) est(g) fu(f)tú(gh)rus,(h.) (;) Quan(h)do(j) ju(k)dex(h) est(g) ven(f)tú(gh)rus,(h.) (;) Cunc(f)ta(e) stric(f)te(d) dis(e)cus(c)sú(d)rus !(d.) (::)
Pi(h)e(j) Je(k)su(h) Dó(g)mi(h)ne,(h.) (;) Do(h)na(g) e(f)is(e) ré(d)qui(c)em.(d.) (::)
A(ded)men.(cd..) (::)""",
            translation = "Jour de colère que ce jour-là, qui réduira le monde en cendres, selon les oracles de David et de la Sibylle. Quelle terreur quand le Juge viendra pour tout examiner dans la rigueur ! Doux Jésus, donne-leur le repos. Amen."
        ),
        offertory = ProperChant(
            part = "Offert.",
            latinTitle = "Domine Jesu Christe",
            vernacularTitle = "Seigneur Jésus-Christ, Roi de gloire",
            mode = "II",
            gabc = """name: Domine Jesu Christe;
office-part: Offertorium;
mode: 2;
book: Graduale Romanum, 1961, p. 96* & The Liber Usualis, 1961, p. 1813;
transcriber: Andrew Hinkley;
%%
(c4) DÓ(f)mi(fe)ne(d) Je(f)su(fg) Chris(g)te,(g.) *(,) Rex(h) gló(g)ri(fe)ae,(d.) (:) lí(f)be(gh)ra(h) á(h)ni(g)mas(f) óm(g)ni(f)um(fe) fi(d)dé(e)li(f)um(g) de(f)func(fe)tó(cd)rum(d.) (;) de(f) poé(gh)nis(h) in(g)fér(fe)ni,(d.) (;) et(f) de(f) pro(fe)fún(d)do(e) la(d)cu.(c.) (::)""",
            translation = "Seigneur Jésus-Christ, Roi de gloire, délivre les âmes de tous les fidèles défunts des peines de l'enfer et de la fosse profonde."
        ),
        communion = ProperChant(
            part = "Comm.",
            latinTitle = "Lux aeterna",
            vernacularTitle = "Que la lumière éternelle brille sur eux",
            mode = "VIII",
            gabc = """name: Lux aeterna;
office-part: Communio;
mode: 8;
book: Graduale Romanum, 1961, p. 99* & The Liber Usualis, 1961, p. 1815;
transcriber: Andrew Hinkley;
commentary: 4 Esdr 2: 35, 34;
%%
(c4) LUX(g) ae(h)tér(j)na(j.) *(,) lú(j)ce(i)at(h) e(j)is,(h) Dó(g)mi(h)ne :(h.) (:) * Cum(j) Sanc(i)tis(h) tu(j)is(h) in(g) ae(fe)tér(d)num,(f.) (;) qui(e)a(f) pi(gh)us(h) es.(g.) (::)
(z) <sp>V/</sp>. Ré(j)qui(j)em(j) ae(j)tér(j)nam(j) do(j)na(j) e(j)is(i) Dó(j)mi(h)ne :(g.) *(:) et(j) lux(j) per(j)pé(j)tu(j)a(j) lú(i)ce(h)at(j) e(h)is.(g.) (::)""",
            translation = "Que la lumière éternelle brille sur eux, Seigneur : * avec tes Saints dans l'éternité, car tu es miséricordieux. ℣. Donne-leur le repos éternel, Seigneur, et que la lumière sans fin brille sur eux."
        )
    )

    val allSets = listOf(
        easterSunday,
        christmasDay,
        epiphany,
        advent1,
        lent1,
        ascension,
        pentecost,
        trinitySunday,
        corpusChristi,
        christTheKing,
        allSaints,
        immaculateConception,
        requiemMass
    )

    fun getForDay(dayLatinTitle: String, season: LiturgicalSeason): ProperSet {
        val lower = dayLatinTitle.lowercase()
        return when {
            lower.contains("defunct") || lower.contains("requiem") -> requiemMass
            lower.contains("resurrec") || lower.contains("pasch") -> easterSunday
            lower.contains("ascensio") -> ascension
            lower.contains("pentecost") -> pentecost
            lower.contains("trinitat") -> trinitySunday
            lower.contains("corporis christi") || lower.contains("fête-dieu") -> corpusChristi
            lower.contains("regis") || lower.contains("christ the king") || lower.contains("christ-roi") -> christTheKing
            lower.contains("omnium sanctorum") || lower.contains("toussaint") -> allSaints
            lower.contains("concept") || lower.contains("immacul") -> immaculateConception
            lower.contains("nativ") || lower.contains("noël") || lower.contains("noel") -> christmasDay
            lower.contains("epiphan") -> epiphany
            lower.contains("advent") -> advent1
            lower.contains("quadrages") || lower.contains("cinér") || lower.contains("ciner") || lower.contains("passio") -> lent1
            season == LiturgicalSeason.EASTERTIDE -> easterSunday
            season == LiturgicalSeason.CHRISTMASTIDE -> christmasDay
            season == LiturgicalSeason.ADVENT -> advent1
            season == LiturgicalSeason.LENT || season == LiturgicalSeason.PASSIONTIDE || season == LiturgicalSeason.SEPTUAGESIMA -> lent1
            else -> trinitySunday
        }
    }
}
