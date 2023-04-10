package com.unciv.ui.screens.multiplayerscreens

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.unciv.Constants
import com.unciv.logic.multiplayer.apiv2.GameOverviewResponse
import com.unciv.ui.components.KeyCharAndCode
import com.unciv.ui.components.NewButton
import com.unciv.ui.components.RefreshButton
import com.unciv.ui.components.extensions.addSeparator
import com.unciv.ui.components.extensions.addSeparatorVertical
import com.unciv.ui.components.extensions.brighten
import com.unciv.ui.components.extensions.keyShortcuts
import com.unciv.ui.components.extensions.onActivation
import com.unciv.ui.components.extensions.onClick
import com.unciv.ui.components.extensions.toLabel
import com.unciv.ui.components.extensions.toTextButton
import com.unciv.ui.popups.CreateLobbyPopup
import com.unciv.ui.popups.Popup
import com.unciv.ui.popups.ToastPopup
import com.unciv.ui.screens.basescreen.BaseScreen
import com.unciv.utils.Log
import com.unciv.ui.components.AutoScrollPane as ScrollPane

/**
 * Screen that should list all open lobbies on the left side, with buttons to interact with them and a list of recently opened games on the right
 */
class LobbyBrowserScreen : BaseScreen() {
    private val lobbyBrowserTable = LobbyBrowserTable(this)
    private val gameList = GameListV2(this, ::onSelect)

    private val table = Table()  // main table including all content of this screen
    private val bottomTable = Table()  // bottom bar including the cancel and help buttons

    private val newLobbyButton = NewButton()
    private val helpButton = "Help".toTextButton()
    private val updateButton = RefreshButton()
    private val closeButton = Constants.close.toTextButton()

    init {
        table.add("Lobby browser".toLabel(fontSize = Constants.headingFontSize)).padTop(20f).padBottom(10f)
        table.add().colspan(2)  // layout purposes only
        table.add("Currently open games".toLabel(fontSize = Constants.headingFontSize)).padTop(20f).padBottom(10f)
        table.row()

        val lobbyButtons = Table()
        newLobbyButton.onClick {
            CreateLobbyPopup(this as BaseScreen)
            // TODO: Testing with random UUID, need a pop-up to determine private/public lobby type
            //game.pushScreen(LobbyScreen(UUID.randomUUID(), UUID.randomUUID()))
        }
        updateButton.onClick {
            lobbyBrowserTable.triggerUpdate()
        }
        lobbyButtons.add(newLobbyButton).padBottom(5f).row()
        lobbyButtons.add("F".toTextButton().apply {
            onClick { ToastPopup("Filtering is not implemented yet", stage) }
        }).padBottom(5f).row()
        lobbyButtons.add(updateButton).row()

        table.add(ScrollPane(lobbyBrowserTable).apply { setScrollingDisabled(true, false) }).growX().growY().padRight(10f)
        table.add(lobbyButtons).padLeft(10f).growY()
        table.addSeparatorVertical(Color.DARK_GRAY, 1f).height(0.75f * stage.height).padLeft(10f).padRight(10f).growY()
        table.add(ScrollPane(gameList).apply { setScrollingDisabled(true, false) }).growX()
        table.row()

        closeButton.keyShortcuts.add(KeyCharAndCode.ESC)
        closeButton.keyShortcuts.add(KeyCharAndCode.BACK)
        closeButton.onActivation {
            game.popScreen()
        }
        helpButton.onClick {
            val helpPopup = Popup(this)
            helpPopup.addGoodSizedLabel("This should become a lobby browser.").row()  // TODO
            helpPopup.addCloseButton()
            helpPopup.open()
        }
        bottomTable.add(closeButton).pad(20f)
        bottomTable.add().colspan(2).growX()  // layout purposes only
        bottomTable.add(helpButton).pad(20f)

        table.addSeparator(skinStrings.skinConfig.baseColor.brighten(0.1f), height = 1f).width(stage.width * 0.85f).padTop(15f).row()
        table.row().bottom().fillX().maxHeight(stage.height / 8)
        table.add(bottomTable).colspan(4).fillX()

        table.setFillParent(true)
        stage.addActor(table)
    }

    private fun onSelect(game: GameOverviewResponse) {
        Log.debug("Selecting game '%s' (%s)", game.name, game.gameUUID)  // TODO: Implement handling
    }

}