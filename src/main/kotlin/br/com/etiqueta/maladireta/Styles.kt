package br.com.etiqueta.maladireta

import javafx.scene.Cursor
import javafx.scene.effect.DropShadow
import javafx.scene.paint.Color
import javafx.scene.text.FontWeight
import tornadofx.Stylesheet
import tornadofx.box
import tornadofx.cssclass
import tornadofx.px

class Styles : Stylesheet() {

    companion object {
        val heading by cssclass()
        val card by cssclass()
        val primaryButton by cssclass()
        val secondaryButton by cssclass()
    }

    init {
        heading {
            fontSize = 24.px
            fontWeight = FontWeight.BOLD
            textFill = Color.web("#1e3a8a")
        }

        card {
            backgroundColor += Color.WHITE
            borderRadius += box(10.px)
            borderColor += box(Color.web("#e5e7eb"))
            borderWidth += box(1.px)
            padding = box(20.px)
            effect = DropShadow(10.0, 0.0, 2.0, Color.rgb(0, 0, 0, 0.1))
        }

        primaryButton {
            backgroundColor += Color.web("#2563eb")
            textFill = Color.WHITE
            fontSize = 14.px
            fontWeight = FontWeight.BOLD
            borderRadius += box(6.px)
            padding = box(10.px, 20.px)
            cursor = Cursor.HAND

            and(hover) {
                backgroundColor += Color.web("#1d4ed8")
            }
        }

        secondaryButton {
            backgroundColor += Color.web("#f3f4f6")
            textFill = Color.web("#374151")
            fontSize = 14.px
            fontWeight = FontWeight.MEDIUM
            borderRadius += box(6.px)
            padding = box(10.px, 20.px)
            cursor = Cursor.HAND

            and(hover) {
                backgroundColor += Color.web("#e5e7eb")
            }
        }
    }
}