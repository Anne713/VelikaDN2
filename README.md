V programskem jeziku Java napišite program, ki omogoča igranje igre "More or less less is more".

Pravila igre:

Igro sestavlja dvodimenzionalno polje velikosti nxn (igralno polje). Vsak element igralnega polja je gumb, ki mu pripada številka.

Inicializacija igre:

-  številke na gumbih igralnega polja se postavijo na naključno cifro med 1 in 9

-  nad igralnim poljem se prikaže ciljna vrednost

-  pod igralnim poljem se izpiše trenutna vsota števil prikazanih na gumbih igralnega polja

-  v zgornjem desnem kotu nad igralnim poljem se prikaže število potez do zaključka igre (to število )

Igranje:

Igralec z izbiro gumba (z vnaprej določeno cifro 1 - 9) izbere možne kandidate za naslednjo izbiro.

Na začetku je Previous neveljaven (beri naprej)- Začetno potezo določi igralec tako, da izbere poljubni gumb (Current) na igralnem polju. Tako omogoči izbiro vseh stolpcev in vseh vrstic, katerih zaporedna številka je deljiva s cifro izbranega gumba.

1. Igralcu je omogočena izbira enega od gumbov, ki je na preseku možnih vrstic in stolpcev izbire Previous in Current. Izbira gumba zamenja vrednost Currect v Previous, Current pa postane vrednost cifre na izbranem gumbu (Previous = Current; Current = Nova vrednost).

2. Povečaj skupni seštevek z vrednostjo Current. Izbran gumb naj postane nedejaven (v igri ga ne moremo več uporabiti).

3. Dekrementiraj število potez in ponovi od točke 1. Ponavljaj dokler je števec števila potez je večji od 0.

Cilj igre:

Vsota števil na izbranih gumbih igralnega polja se mora čim bolj približati ciljni vrednosti.

Naloga zahteva: (v oklepajih točke)

- izdelavo uporabniškega vmesnika, ki omogoča igranje (vsakič drugo igralno polje), z vsakim novim zagonom igre dobimo novo postavitev (n se izbere naključno). (50 točk)

- izdelavo uporabniškega vmesnika, ki igralno polje shrani v datoteko (ob igranju, omogoča izbiro postavitve igralnega polje, iz datoteke) (15 točk)

- meni za nadzor igre (ponovna igra, nastavitev velikosti igralnega polja, nastavitev števila potez, nastavitev ciljne vrednosti ). (10 točk)

- nadgradnja menija za nadzor igre: možnost izbire težavnosti (easy, medium, hard), nastavljena težavnost naj vpliva na ciljno vrednost in na število potez do zaključka igre. (10 točk)

- ob izteku števca števila potez naj grafični uporabniški vmesnik prikaže igralcu odmik od končne vrednosti (kakovost rešitve. (5 točk)

- igra naj sama ugotovi, ko ni več možnega nadaljevanja. (10 točk)

- DODATNE TOČKE (tudi prek 100%):

  - grafični uporabniški vmesnik naj nudi uporabniku pomoč pri izbiri gumba B v točki Igranje 1. tako, da prikaže novo vrednost, ki zavzame gumb A, če bi uporabnik izbral neki možni gumb B. (10)

  - dodaj možnost, da grafični uporabniški vmesnik predlaga izbiro gumba B v točki Igranje 1. Predlagan gumb B naj bo dobra poteza, ki omogoči uporabniku se približati ciljni vrednosti. (5)

PAZI!

- Pazite na pravilno uporabo osnovnih elementov predmetno-naravnanega programiranja - statične metode so prepovedane, posamezne module programa definirajte kot samostojne razrede. (do -5)

- Pazite na pravilno uporabo pogodb! (do -5)

Oddaja:

Oddaj .zip arhiv, ki vsebuje projekt.