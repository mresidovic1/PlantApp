package com.example.plantapp

import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.contrib.RecyclerViewActions.scrollToPosition
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.hamcrest.CoreMatchers.any
import org.hamcrest.CoreMatchers.anyOf
import org.hamcrest.CoreMatchers.anything
import org.hamcrest.CoreMatchers.not
import org.hamcrest.Matchers
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class Test {

    @get:Rule
    var activityScenarioRule = ActivityScenarioRule(
        NovaBiljkaActivity::class.java
    )

    @Test
    fun testKratakNaziv() {
        onView(withId(R.id.nazivET)).perform(typeText("da"), closeSoftKeyboard())
        onView(withId(R.id.dodajBiljkuBtn)).perform(scrollTo(), click())
        onView(withId(R.id.nazivET)).check(matches(hasErrorText("Riječ mora sadržavati više od 2, a manje od 20 karaktera")))
    }

    @Test
    fun predugNaziv() {
        onView(withId(R.id.nazivET)).perform(typeText("nazivnazivnazivnazivnaziv"), closeSoftKeyboard())
        onView(withId(R.id.dodajBiljkuBtn)).perform(scrollTo(), click())
        onView(withId(R.id.nazivET)).check(matches(hasErrorText("Riječ mora sadržavati više od 2, a manje od 20 karaktera")))
    }

    @Test
    fun testKratkaPorodica() {
        onView(withId(R.id.porodicaET)).perform(typeText("a"), closeSoftKeyboard())
        onView(withId(R.id.dodajBiljkuBtn)).perform(scrollTo(), click())
        onView(withId(R.id.porodicaET)).check(matches(hasErrorText("Riječ mora sadržavati više od 2, a manje od 20 karaktera")))
    }

    @Test
    fun predugaPorodica() {
        onView(withId(R.id.porodicaET)).perform(typeText("porodicaporodicaporodica"), closeSoftKeyboard())
        onView(withId(R.id.dodajBiljkuBtn)).perform(scrollTo(), click())
        onView(withId(R.id.porodicaET)).check(matches(hasErrorText("Riječ mora sadržavati više od 2, a manje od 20 karaktera")))
    }

    @Test
    fun testKratkoUpozorenje() {
        onView(withId(R.id.medicinskoUpozorenjeET)).perform(typeText(" "), closeSoftKeyboard())
        onView(withId(R.id.dodajBiljkuBtn)).perform(scrollTo(), click())
        onView(withId(R.id.medicinskoUpozorenjeET)).check(matches(hasErrorText("Riječ mora sadržavati više od 2, a manje od 20 karaktera")))
    }

    @Test
    fun predugoUpozorenje() {
        onView(withId(R.id.medicinskoUpozorenjeET)).perform(typeText("upozorenjeupozorenjeupozorenje"), closeSoftKeyboard())
        onView(withId(R.id.dodajBiljkuBtn)).perform(scrollTo(), click())
        onView(withId(R.id.medicinskoUpozorenjeET)).check(matches(hasErrorText("Riječ mora sadržavati više od 2, a manje od 20 karaktera")))
    }

    @Test
    fun testKratkoJelo() {
        onView(withId(R.id.jeloET)).perform(typeText("j"), closeSoftKeyboard())
        onView(withId(R.id.dodajBiljkuBtn)).perform(scrollTo(), click())
        onView(withId(R.id.jeloET)).check(matches(hasErrorText("Riječ mora sadržavati više od 2, a manje od 20 karaktera")))
    }

    @Test
    fun predugoJelo() {
        onView(withId(R.id.jeloET)).perform(typeText("jelojelojelojelojelojelojelojelojelo"), closeSoftKeyboard())
        onView(withId(R.id.dodajBiljkuBtn)).perform(scrollTo(), click())
        onView(withId(R.id.jeloET)).check(matches(hasErrorText("Riječ mora sadržavati više od 2, a manje od 20 karaktera")))
    }

    @Test
    fun normalanNaziv() {
        onView(withId(R.id.nazivET)).perform(typeText("normalnaRijec"), closeSoftKeyboard())
        onView(withId(R.id.dodajBiljkuBtn)).perform(scrollTo(), click())
        onView(withId(R.id.nazivET)).check(matches(Matchers.not(hasErrorText("Riječ mora sadržavati više od 2, a manje od 20 karaktera"))))
    }

    @Test
    fun testUslikajBiljkuButtonClick() {
        onView(withId(R.id.uslikajBiljkuBtn)).perform(scrollTo(), click())
        onView(withId(R.id.slikaIV)).check(matches(isDisplayed()))
    }

    @Test
    fun testValidacijaKlimatskiTipLV() {
        onView(withId(R.id.nazivET)).perform(typeText("naziv"), closeSoftKeyboard())
        onView(withId(R.id.porodicaET)).perform(typeText("porodica"), closeSoftKeyboard())
        onView(withId(R.id.medicinskoUpozorenjeET)).perform(typeText("upozorenje"), closeSoftKeyboard())
        onView(withId(R.id.jeloET)).perform(typeText("jelo"), closeSoftKeyboard())
        onView(withId(R.id.dodajJeloBtn)).perform(click())
        onData(anything())
            .inAdapterView(withId(R.id.medicinskaKoristLV))
            .atPosition(0)
            .perform(scrollTo(),click())
        onView(withId(R.id.zemljisniTipLV)).perform(scrollTo())
        onData(anything())
            .inAdapterView(withId(R.id.zemljisniTipLV))
            .atPosition(0)
            .perform(scrollTo(),click())
        onView(withId(R.id.jelaLV)).perform(scrollTo())
        onData(anything())
            .inAdapterView(withId(R.id.jelaLV))
            .atPosition(0)
            .perform(scrollTo(),click())
        onView(withId(R.id.profilOkusaLV)).perform(scrollTo())
        onData(anything())
            .inAdapterView(withId(R.id.profilOkusaLV))
            .atPosition(0)
            .perform(scrollTo(),click())
        onView(withId(R.id.dodajBiljkuBtn)).perform(scrollTo(), click())
        onView(withText("Mora biti odabran barem jedan klimatski tip.")).check(matches(isDisplayed()))
    }

    @Test
    fun testValidacijaMedicinskaKoristLV() {
        onView(withId(R.id.nazivET)).perform(typeText("naziv"), closeSoftKeyboard())
        onView(withId(R.id.porodicaET)).perform(typeText("porodica"), closeSoftKeyboard())
        onView(withId(R.id.medicinskoUpozorenjeET)).perform(typeText("upozorenje"), closeSoftKeyboard())
        onView(withId(R.id.jeloET)).perform(typeText("jelo"), closeSoftKeyboard())
        onView(withId(R.id.dodajJeloBtn)).perform(click())
        onView(withId(R.id.klimatskiTipLV)).perform(scrollTo())
        onData(anything())
            .inAdapterView(withId(R.id.klimatskiTipLV))
            .atPosition(0)
            .perform(scrollTo(),click())
        onView(withId(R.id.jelaLV)).perform(scrollTo())
        onData(anything())
            .inAdapterView(withId(R.id.jelaLV))
            .atPosition(0)
            .perform(scrollTo(),click())
        onView(withId(R.id.zemljisniTipLV)).perform(scrollTo())
        onData(anything())
            .inAdapterView(withId(R.id.zemljisniTipLV))
            .atPosition(0)
            .perform(scrollTo(),click())
        onView(withId(R.id.profilOkusaLV)).perform(scrollTo())
        onData(anything())
            .inAdapterView(withId(R.id.profilOkusaLV))
            .atPosition(0)
            .perform(scrollTo(),click())
        onView(withId(R.id.dodajBiljkuBtn)).perform(scrollTo(), click())
        onView(withText("Mora biti odabrana barem jedna medicinska korist.")).check(matches(isDisplayed()))
    }

    @Test
    fun testValidacijaZemljisniTipLV() {
        onView(withId(R.id.nazivET)).perform(typeText("naziv"), closeSoftKeyboard())
        onView(withId(R.id.porodicaET)).perform(typeText("porodica"), closeSoftKeyboard())
        onView(withId(R.id.medicinskoUpozorenjeET)).perform(typeText("upozorenje"), closeSoftKeyboard())
        onView(withId(R.id.jeloET)).perform(typeText("jelo"), closeSoftKeyboard())
        onView(withId(R.id.dodajJeloBtn)).perform(click())
        onData(anything())
            .inAdapterView(withId(R.id.medicinskaKoristLV))
            .atPosition(0)
            .perform(scrollTo(),click())
        onView(withId(R.id.klimatskiTipLV)).perform(scrollTo())
        onData(anything())
            .inAdapterView(withId(R.id.klimatskiTipLV))
            .atPosition(0)
            .perform(scrollTo(),click())
        onView(withId(R.id.jelaLV)).perform(scrollTo())
        onData(anything())
            .inAdapterView(withId(R.id.jelaLV))
            .atPosition(0)
            .perform(scrollTo(),click())
        onView(withId(R.id.profilOkusaLV)).perform(scrollTo())
        onData(anything())
            .inAdapterView(withId(R.id.profilOkusaLV))
            .atPosition(0)
            .perform(scrollTo(),click())
        onView(withId(R.id.dodajBiljkuBtn)).perform(scrollTo(), click())
        onView(withText("Mora biti odabran barem jedan zemljišni tip.")).check(matches(isDisplayed()))
    }

    @Test
    fun testValidacijaProfilOkusa() {
        onView(withId(R.id.nazivET)).perform(typeText("naziv"), closeSoftKeyboard())
        onView(withId(R.id.porodicaET)).perform(typeText("porodica"), closeSoftKeyboard())
        onView(withId(R.id.medicinskoUpozorenjeET)).perform(typeText("upozorenje"), closeSoftKeyboard())
        onView(withId(R.id.jeloET)).perform(typeText("jelo"), closeSoftKeyboard())
        onView(withId(R.id.dodajJeloBtn)).perform(click())
        onData(anything())
            .inAdapterView(withId(R.id.medicinskaKoristLV))
            .atPosition(0)
            .perform(scrollTo(),click())
        onView(withId(R.id.klimatskiTipLV)).perform(scrollTo())
        onData(anything())
            .inAdapterView(withId(R.id.klimatskiTipLV))
            .atPosition(0)
            .perform(scrollTo(),click())
        onView(withId(R.id.jelaLV)).perform(scrollTo())
        onData(anything())
            .inAdapterView(withId(R.id.jelaLV))
            .atPosition(0)
            .perform(scrollTo(),click())
        onView(withId(R.id.zemljisniTipLV)).perform(scrollTo())
        onData(anything())
            .inAdapterView(withId(R.id.zemljisniTipLV))
            .atPosition(0)
            .perform(scrollTo(),click())
        onView(withId(R.id.dodajBiljkuBtn)).perform(scrollTo(), click())
        onView(withText("Morate odabrati profil okusa.")).check(matches(isDisplayed()))
    }

    @Test
    fun testValidacijaDodavanjeJela() {
        onView(withId(R.id.jeloET)).perform(typeText("ISTOJELO"), closeSoftKeyboard())
        onView(withId(R.id.dodajJeloBtn)).perform(click())
        onView(withId(R.id.jeloET)).perform(typeText("istojelo"), closeSoftKeyboard())
        onView(withId(R.id.dodajJeloBtn)).perform(click())
        onView(withId(R.id.jeloET)).check(matches(hasErrorText("Jelo je već dodano u listu!")))
    }

    @Test
    fun testValidacijaJela() {
        onView(withId(R.id.nazivET)).perform(typeText("naziv"), closeSoftKeyboard())
        onView(withId(R.id.porodicaET)).perform(typeText("porodica"), closeSoftKeyboard())
        onView(withId(R.id.medicinskoUpozorenjeET)).perform(typeText("upozorenje"), closeSoftKeyboard())
        onData(anything())
            .inAdapterView(withId(R.id.medicinskaKoristLV))
            .atPosition(0)
            .perform(scrollTo(),click())
        onView(withId(R.id.klimatskiTipLV)).perform(scrollTo())
        onData(anything())
            .inAdapterView(withId(R.id.klimatskiTipLV))
            .atPosition(0)
            .perform(scrollTo(),click())
        onView(withId(R.id.zemljisniTipLV)).perform(scrollTo())
        onData(anything())
            .inAdapterView(withId(R.id.zemljisniTipLV))
            .atPosition(0)
            .perform(scrollTo(),click())
        onView(withId(R.id.profilOkusaLV)).perform(scrollTo())
        onData(anything())
            .inAdapterView(withId(R.id.profilOkusaLV))
            .atPosition(0)
            .perform(scrollTo(),click())
        onView(withId(R.id.dodajBiljkuBtn)).perform(scrollTo(), click())
        onView(withText("Morate dodati barem jedno jelo.")).check(matches(isDisplayed()))
    }
}
