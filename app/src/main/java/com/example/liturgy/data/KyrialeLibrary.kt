package com.example.liturgy.data

import com.example.liturgy.model.KyrialeSetting

data class GregorianChantItem(
    val id: String,
    val title: String,
    val latinTitle: String,
    val part: String, // Kyrie, Gloria, Sanctus, etc.
    val mode: String,
    val gabc: String,
    val translation: String,
    val setting: KyrialeSetting? = null
)

object KyrialeLibrary {

    val aspergesMe = GregorianChantItem(
        id = "asperges_me",
        title = "Asperges me",
        latinTitle = "Asperges me, Domine",
        part = "Asperges",
        mode = "VII",
        gabc = """(c2) A(cd)spér(fe)ges(d) me,(ef) *(,) Dó(g)mi(f)ne,(f) hys(fd)só(f)po,(g) et(f) mun(e)dá(d)bor :(d.) (:) la(cd)vá(fe)bis(d) me,(ef) (,) et(g) su(f)per(f) ni(fd)vem(f) de(g)al(f)bá(e)bor.(d.) (::)
(z) <sp>V/</sp>. Mi(h)se(gh)ré(h)re(h) me(h)i,(h) De(hg)us,(gh..) *(:) se(h)cún(h)dum(h) ma(h)gnam(h) mi(h)se(h)ri(h)cór(h)di(h)am(g) tu(h)am.(h.) (::)
Gló(h)ri(gh)a(h) Pa(h)tri,(h) et(h) Fí(h)li(h)o,(h) et(h) Spi(h)rí(h)tu(h)i(g) Sanc(gh)to.(h.) *(:) Sic(h)ut(h) e(h)rat(h) in(h) prin(h)cí(h)pi(h)o,(h) et(h) nunc,(h) et(g) sem(gh)per,(h.) (:) et(h) in(h) saé(h)cu(h)la(h) sae(h)cu(h)ló(h)rum.(g) A(h)men.(h.) (::)""",
        translation = "Purifie-moi avec l'hysope, Seigneur, et je serai pur ; lave-moi, et je serai plus blanc que la neige. Aie pitié de moi, ô Dieu, selon ta grande miséricorde."
    )

    val vidiAquam = GregorianChantItem(
        id = "vidi_aquam",
        title = "Vidi aquam (Temps Pascal)",
        latinTitle = "Vidi aquam egredientem",
        part = "Asperges",
        mode = "VIII",
        gabc = """(c3) VI(ef)di(fd) a(fh)quam(h) *(,) e(e)gre(f)di(h)én(hi)tem(i) de(i) tem(h!ij)plo,(i.) (;) a(h) la(ij)te(i)re(h) dex(hg)tro,(f!gh) al(g)le(e)lú(ef)ia :(e.) (:) et(f) om(ef)nes,(d) ad(fh) quos(h) per(h)vé(hi)nit(i) a(i)qua(h) i(h!ij)sta,(i.) (;) sal(h)vi(ij) fac(i)ti(h) sunt,(hg/f!gh) (,) et(g) di(e)cent,(ef) al(g)le(f)lú(e)ia,(d) al(ef)le(f)lú(e)ia.(e.) (::)
(z) <sp>V/</sp>. Con(e)fi(fe)té(eh)mi(h)ni(h) Dó(h)mi(h)no(h) quó(h)ni(h)am(h) bo(h)nus :(i.) *(:) quó(h)ni(h)am(h) in(h) saé(h)cu(h)lum(h) mi(h)se(h)ri(h)cór(g)di(h)a(f) e(e)jus.(e.) (::)""",
        translation = "J'ai vu l'eau jaillissant du temple, du côté droit, alléluia : et tous ceux vers qui est venue cette eau ont été sauvés, et ils diront : alléluia, alléluia."
    )

    val kyrieDeAngelis = GregorianChantItem(
        id = "kyrie_viii",
        title = "Kyrie VIII (De Angelis)",
        latinTitle = "Kyrie eleison",
        part = "Kyrie",
        mode = "V",
        gabc = """(c3) KY(d)ri(ef)e(f!gh) * e(gf)lé(e)i(fg)son.(f.) <i>bis</i>(::)
Chris(fe)te(fh/ih) e(gf)lé(e)i(fg)son.(f.) <i>bis</i>(::)
Ký(f!gh)ri(gf)e(e!fg) e(gf)lé(e)i(fg)son.(f.) (:) Ký(d)ri(ef)e(f!gh) * e(gf)lé(e)i(fg)son.(f.) (::)""",
        translation = "Seigneur, prends pitié. Christ, prends pitié. Seigneur, prends pitié.",
        setting = KyrialeSetting.KYRIALE_VIII
    )

    val sanctusDeAngelis = GregorianChantItem(
        id = "sanctus_viii",
        title = "Sanctus VIII (De Angelis)",
        latinTitle = "Sanctus, Sanctus, Sanctus",
        part = "Sanctus",
        mode = "VI",
        gabc = """(c4) SAnc(f)tus,(fg/hgh) * Sanc(fg/hgh)tus,(f.) (;) Sanc(f!gh)tus(h) Dó(g)mi(f)nus(ef) De(g)us(f) Sá(e)ba(d)oth.(c.) (:) Ple(f)ni(fe) sunt(d) cae(ef)li(f) et(f) ter(gh)ra(h.) (;) gló(g)ri(f)a(ef) tu(g)a.(f.) (:) Ho(f!gh)sán(hvGF)na(e) in(f) ex(g)cél(e)sis.(d.) (:) Be(f)ne(ef)díc(g)tus(f) qui(fe) ve(d)nit(c.) (;) in(e) nó(f)mi(g)ne(f) Dó(e)mi(d)ni.(c.) (:) Ho(f!gh)sán(hvGF)na(e) in(f) ex(g)cél(e)sis.(d.) (::)""",
        translation = "Saint, Saint, Saint, le Seigneur, Dieu de l'univers. Le ciel et la terre sont remplis de ta gloire. Hosanna au plus haut des cieux. Béni soit celui qui vient au nom du Seigneur.",
        setting = KyrialeSetting.KYRIALE_VIII
    )

    val agnusDeAngelis = GregorianChantItem(
        id = "agnus_viii",
        title = "Agnus Dei VIII (De Angelis)",
        latinTitle = "Agnus Dei",
        part = "Agnus Dei",
        mode = "VI",
        gabc = """(c4) A(f)gnus(ghg) De(f)i,(e.) * qui(f) tol(gh)lis(h) pec(gf)cá(g)ta(fe) mun(d)di :(c.) (;) mi(f)se(fe)ré(d)re(ef) no(g)bis.(f.) (::)
A(f)gnus(ghg) De(f)i,(e.) * qui(f) tol(gh)lis(h) pec(gf)cá(g)ta(fe) mun(d)di :(c.) (;) mi(f)se(fe)ré(d)re(ef) no(g)bis.(f.) (::)
A(f)gnus(ghg) De(f)i,(e.) * qui(f) tol(gh)lis(h) pec(gf)cá(g)ta(fe) mun(d)di :(c.) (;) do(f)na(fe) no(d)bis(ef) pa(g)cem.(f.) (::)""",
        translation = "Agneau de Dieu, qui enlèves les péchés du monde, prends pitié de nous. Agneau de Dieu, qui enlèves les péchés du monde, donne-nous la paix.",
        setting = KyrialeSetting.KYRIALE_VIII
    )

    val kyrieLuxEtOrigo = GregorianChantItem(
        id = "kyrie_i",
        title = "Kyrie I (Lux et origo - Temps Pascal)",
        latinTitle = "Kyrie eleison",
        part = "Kyrie",
        mode = "VIII",
        gabc = """(c4) KY(g)ri(g)e(fvED) * e(ef)lé(g)i(f)son.(e.) <i>bis</i>(::)
Chris(g)te(h!ij) e(j)lé(i)i(h)son.(g.) <i>bis</i>(::)
Ký(g)ri(g)e(fvED) e(ef)lé(g)i(f)son.(e.) (:) Ký(g)ri(g)e(fvED) * (ef/gh) e(gf)lé(e)i(fg)son.(e.) (::)""",
        translation = "Seigneur, prends pitié. Christ, prends pitié. Seigneur, prends pitié.",
        setting = KyrialeSetting.KYRIALE_I
    )

    val kyrieOrbisFactor = GregorianChantItem(
        id = "kyrie_xi",
        title = "Kyrie XI (Orbis factor - Dimanches)",
        latinTitle = "Kyrie eleison",
        part = "Kyrie",
        mode = "I",
        gabc = """(c4) KY(d)ri(dfED)e(c) * e(cd)lé(f)i(e)son.(d.) <i>bis</i>(::)
Chris(fgFE)te(d) e(fg)lé(g)i(f)son.(e.) <i>bis</i>(::)
Ký(d)ri(dfED)e(c) e(cd)lé(f)i(e)son.(d.) (:) Ký(ixhiHG)ri(f)e(e!fg) * e(fED)lé(cd)i(e)son.(d.) (::)""",
        translation = "Seigneur, prends pitié. Christ, prends pitié. Seigneur, prends pitié.",
        setting = KyrialeSetting.KYRIALE_XI
    )

    val kyrieCumJubilo = GregorianChantItem(
        id = "kyrie_ix",
        title = "Kyrie IX (Cum jubilo - Fêtes Mariales)",
        latinTitle = "Kyrie eleison",
        part = "Kyrie",
        mode = "I",
        gabc = """(c4) KY(df)ri(fe)e(d) * e(fvED)lé(c)i(d)son.(d.) <i>bis</i>(::)
Chris(df)te(fe/dc) e(fvED)lé(c)i(d)son.(d.) <i>bis</i>(::)
Ký(df)ri(fe)e(d) e(fvED)lé(c)i(d)son.(d.) (:) Ký(f!gh)ri(hg)e(f) * (gh/fe/dc) e(fvED)lé(c)i(d)son.(d.) (::)""",
        translation = "Seigneur, prends pitié. Christ, prends pitié. Seigneur, prends pitié.",
        setting = KyrialeSetting.KYRIALE_IX
    )

    val credoIII = GregorianChantItem(
        id = "credo_iii",
        title = "Credo III",
        latinTitle = "Credo in unum Deum",
        part = "Credo",
        mode = "V",
        gabc = """(c3) CRe(d)do(ef) in(f) u(e)num(f) De(g)um,(f.) *(::) Pa(f)trem(gh) om(h)ni(g)po(f)tén(g)tem,(f.) (;) fac(f)tó(ef)rem(d) cae(c)li(d) et(ef) ter(g)rae,(f.) (;) vi(f)si(e)bí(f)li(g)um(f) óm(e)ni(d)um,(c.) (;) et(e) in(f)vi(g)si(f)bí(e)li(d)um.(d.) (::)
Et(d) in(ef) u(f)num(e) Dó(f)mi(g)num(f.) (,) Je(f)sum(gh) Chris(h)tum,(g.) (;) Fí(f)li(e)um(f) De(g)i(f) u(e)ni(d)gé(c)ni(d)tum.(d.) (::)""",
        translation = "Je crois en un seul Dieu, le Père tout-puissant, créateur du ciel et de la terre, de l'univers visible et invisible. Et en un seul Seigneur, Jésus-Christ, Fils unique de Dieu."
    )

    val allChants = listOf(
        aspergesMe,
        vidiAquam,
        kyrieDeAngelis,
        sanctusDeAngelis,
        agnusDeAngelis,
        kyrieLuxEtOrigo,
        kyrieOrbisFactor,
        kyrieCumJubilo,
        credoIII
    )

    fun getChantsForSetting(setting: KyrialeSetting): List<GregorianChantItem> {
        return allChants.filter { it.setting == setting }
    }
}
