package com.ourshipsgame.game;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.ourshipsgame.chess_pieces.*;
import com.ourshipsgame.handlers.Constant;
import com.ourshipsgame.handlers.Score;
import com.ourshipsgame.inteligentSystems.ComputerPlayerAi;

import static com.ourshipsgame.game.GameBoard.BoardLocations.*;

/**
 * Klasa abstrakcyjna zawierająca metody oraz obiekty i zmienne niezbędne do
 * funkcjonowania aplikacji
 */
public abstract class GameEngine extends ScreenAdapter implements Constant {

    // Important vars
    protected GameBoard gameBoard = new GameBoard();
    /**
     * Obiekt przechowujący informacje o wynikach gracza
     */
    protected Score PlayerOne = new Score(1);
    /**
     * Obiekt przechowujący informacje o wynikach komputera
     */
    protected Score PlayerTwo = new Score(2);
    /**
     * Obiekt obliczający decyzje komputera
     */
    protected ComputerPlayerAi enemyComputerPlayerAi;
    /**
     * Zmienna okreslająca czyja tura jest aktualnie
     */
    protected int PlayerTurn;
    /**
     * Tablica kursorów
     */
    protected Cursor[] crosshairs = new Cursor[3];
    /**
     * Kursor
     */
    protected Cursor cursor;
    /**
     * Pixmapa kursorów
     */
    protected Pixmap[] crosshairPixmaps = new Pixmap[3];

    /**
     * Zmienna przechowująca wysokość okna w pikselach
     */
    protected int gameHeight = GAME_HEIGHT;
    /**
     * Zmienna przechowująca szerokość okna w pikselach
     */
    protected int gameWidth = GAME_WIDTH;
    /**
     * Zmienna przechowująca wysokość okna w pikselach
     */
    protected float gameHeight_f = GAME_HEIGHT_F;
    /**
     * Zmienna przechowująca szerokość okna w pikselach
     */
    protected float gameWidth_f = GAME_WIDTH_F;
    /**
     * Czcionka do interfejsu
     */
    protected BitmapFont hudFont;
    /**
     * Czcionka do tekstu tury nieaktywnej
     */
    protected BitmapFont turnFont;
    /**
     * Czcionka do tekstu tury aktywnej
     */
    protected BitmapFont turnFontActive;
    // Sounds and music
    /**
     * Tablica dźwięków końcowych
     */
    protected Sound[] endSounds = new Sound[2];
    /**
     * Dźwięk ruchu pionków
     */
    protected Sound moveSound;

    // Important Objects
    /**
     * Tablica obiektów przechowujących białe szachy
     */
    protected Chess[] whiteChesses = new Chess[16];
    /**
     * Tablica obiektów przechowujących czarne szachy
     */
    protected Chess[] blackChesses = new Chess[16];

    // more other vars
    /**
     * Zmienna do logiki click n drop statku w czasie ustawiania statków na planszy
     */
    protected int activeSpriteDrag = 99;
    /**
     * Zmienna przechowująca pozycje x sprite'a
     */
    protected float xSprite;
    /**
     * Zmienna przechowująca pozycje y sprite'a
     */
    protected float ySprite;
    /**
     * Zmienna przechowująca róznicę w pozycji w osi X
     */
    protected float xDiff;
    /**
     * Zmienna przechowująca róznicę w pozycji w osi Y
     */
    protected float yDiff;
    /**
     * Zmienna określająca czy Gracz przegrał
     */
    protected boolean PlayerOneLost = false;
    /**
     * Zmienna określająca czy Komputer przegrał
     */
    protected boolean PlayerTwoLost = false;

    /**
     * Metoda do zmiany tury
     */
    protected void switchTurn() {
        if (PlayerTurn == 1)
            PlayerTurn = 2;
        else
            PlayerTurn = 1;
    }

    /**
     * Metoda do ładowania assetów gry do AssetManagera
     *
     * @param manager AssetManager
     */
    // loading method
    protected void loadGameEngine(AssetManager manager) {
        // Crosshairs
        manager.load("core/assets/cursors/crosshairRed.png", Pixmap.class);
        manager.load("core/assets/cursors/crosshairGreen.png", Pixmap.class);
        manager.load("core/assets/ui/ui.hud/cursors/test.png", Pixmap.class);

        // Sound effects
        manager.load("core/assets/sounds/won.mp3", Sound.class);
        manager.load("core/assets/sounds/lose.mp3", Sound.class);

        //Board Textures
        manager.load("core/assets/backgroundtextures/chessBoard.jpg", Texture.class);
        //Chess pieces Textures
        for (int i = 0; i < 12; i++)
            manager.load(ChessPiecesTexturesPaths[i],Texture.class);
    }

    /**
     * Metoda do ładowania assetów interfejsu do AssetManagera
     *
     * @param manager AssetManager
     */
    protected void loadHudAssets(AssetManager manager) {
        // Skin
        manager.load("core/assets/buttons/skins/golden-spiral/skin/golden-ui-skin.json", Skin.class);

        // Options button
        manager.load("core/assets/ui/ui.hud/ui/global/modern/gear.png", Texture.class);
        manager.load("core/assets/ui/ui.hud/ui/global/modern/gear-press.png", Texture.class);
        // Play button
        manager.load("core/assets/ui/ready-button.png", Texture.class);
        manager.load("core/assets/ui/ready-button-pressed.png", Texture.class);
        manager.load("core/assets/ui/ready-button-go.png", Texture.class);
        // Repeat button
        manager.load("core/assets/ui/reverse-button-pressed.png", Texture.class);
        manager.load("core/assets/ui/reverse-button.png", Texture.class);
        // TTF Font
        manager.setLoader(FreeTypeFontGenerator.class,
                new FreeTypeFontGeneratorLoader(new InternalFileHandleResolver()));
        manager.setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(new InternalFileHandleResolver()));
        FreetypeFontLoader.FreeTypeFontLoaderParameter param = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
        param.fontFileName = "core/assets/fonts/nunito.light.ttf";
        param.fontParameters.size = 28;
        param.fontParameters.color = Color.GRAY;
        param.fontParameters.borderColor = Color.DARK_GRAY;
        param.fontParameters.borderWidth = 2;
        manager.load("core/assets/fonts/nunito.light.ttf", BitmapFont.class, param);
        FreetypeFontLoader.FreeTypeFontLoaderParameter param2 = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
        param2.fontFileName = "core/assets/fonts/nunito.light2.ttf";
        param2.fontParameters.size = 28;
        param2.fontParameters.color = Color.GOLD;
        param2.fontParameters.borderColor = Color.DARK_GRAY;
        param2.fontParameters.borderWidth = 2;
        manager.load("core/assets/fonts/nunito.light2.ttf", BitmapFont.class, param2);
    }

    /**
     * Metoda do utworzenia faktycznych obiektów i zmiennych do gry
     *
     * @param computerEnemy Określa czy przeciwnik to komputer
     * @param manager       AssetManager przechowuje zasoby załadowane
     * @return boolean Zwraca true po zakończeniu
     */
    // game methods below
    // Stage 1
    protected boolean preparation(boolean computerEnemy, AssetManager manager) {
        boolean done = false;

        crosshairPixmaps[0] = manager.get("core/assets/cursors/crosshairRed.png", Pixmap.class);
        crosshairPixmaps[1] = manager.get("core/assets/cursors/crosshairGreen.png", Pixmap.class);
        crosshairPixmaps[2] = manager.get("core/assets/ui/ui.hud/cursors/test.png", Pixmap.class);

        endSounds[0] = manager.get("core/assets/sounds/won.mp3", Sound.class);
        endSounds[1] = manager.get("core/assets/sounds/lose.mp3", Sound.class);

        turnFont = manager.get("core/assets/fonts/nunito.light.ttf", BitmapFont.class);
        turnFontActive = manager.get("core/assets/fonts/nunito.light2.ttf", BitmapFont.class);

        gameBoard.Board = new GameObject(
                manager.get("core/assets/backgroundtextures/chessBoard.jpg",Texture.class),
                Constant.X_AXIS_BOARD_START,
                Constant.Y_AXIS_BOARD_START,
                true,
                false,
                null
        );

        whiteChesses[ChessPiecesInArray.King.ordinal()] = new King(
                manager.get(ChessPiecesTexturesPaths[ChessPiecesPaths.W_KING.ordinal()],Texture.class),
                E1.getPosition().getX(),
                E1.getPosition().getY()
        );

        whiteChesses[ChessPiecesInArray.Queen.ordinal()] = new Queen(
                manager.get(ChessPiecesTexturesPaths[ChessPiecesPaths.W_QUEEN.ordinal()],Texture.class),
                D1.getPosition().getX(),
                D1.getPosition().getY()
        );

        whiteChesses[ChessPiecesInArray.FstBishop.ordinal()] = new Bishop(
                manager.get(ChessPiecesTexturesPaths[ChessPiecesPaths.W_BISHOP.ordinal()],Texture.class),
                C1.getPosition().getX(),
                C1.getPosition().getY()
        );

        whiteChesses[ChessPiecesInArray.SndBishop.ordinal()] = new Bishop(
                manager.get(ChessPiecesTexturesPaths[ChessPiecesPaths.W_BISHOP.ordinal()],Texture.class),
                F1.getPosition().getX(),
                F1.getPosition().getY()
        );

        whiteChesses[ChessPiecesInArray.FstKnight.ordinal()] = new Knight(
                manager.get(ChessPiecesTexturesPaths[ChessPiecesPaths.W_KNIGHT.ordinal()],Texture.class),
                B1.getPosition().getX(),
                B1.getPosition().getY()
        );

        whiteChesses[ChessPiecesInArray.SndKnight.ordinal()] = new Knight(
                manager.get(ChessPiecesTexturesPaths[ChessPiecesPaths.W_KNIGHT.ordinal()],Texture.class),
                G1.getPosition().getX(),
                G1.getPosition().getY()
        );

        whiteChesses[ChessPiecesInArray.FstRook.ordinal()] = new Rook(
                manager.get(ChessPiecesTexturesPaths[ChessPiecesPaths.W_ROOK.ordinal()],Texture.class),
                A1.getPosition().getX(),
                A1.getPosition().getY()
        );

        whiteChesses[ChessPiecesInArray.SndRook.ordinal()] = new Rook(
                manager.get(ChessPiecesTexturesPaths[ChessPiecesPaths.W_ROOK.ordinal()],Texture.class),
                H1.getPosition().getX(),
                H1.getPosition().getY()
        );

        whiteChesses[ChessPiecesInArray.Pawn1.ordinal()] = new Pawn(
                manager.get(ChessPiecesTexturesPaths[ChessPiecesPaths.W_PAWN.ordinal()],Texture.class),
                A2.getPosition().getX(),
                A2.getPosition().getY()
        );
        whiteChesses[ChessPiecesInArray.Pawn2.ordinal()] = new Pawn(
                manager.get(ChessPiecesTexturesPaths[ChessPiecesPaths.W_PAWN.ordinal()],Texture.class),
                B2.getPosition().getX(),
                B2.getPosition().getY()
        );
        whiteChesses[ChessPiecesInArray.Pawn3.ordinal()] = new Pawn(
                manager.get(ChessPiecesTexturesPaths[ChessPiecesPaths.W_PAWN.ordinal()],Texture.class),
                C2.getPosition().getX(),
                C2.getPosition().getY()
        );
        whiteChesses[ChessPiecesInArray.Pawn4.ordinal()] = new Pawn(
                manager.get(ChessPiecesTexturesPaths[ChessPiecesPaths.W_PAWN.ordinal()],Texture.class),
                D2.getPosition().getX(),
                D2.getPosition().getY()
        );
        whiteChesses[ChessPiecesInArray.Pawn5.ordinal()] = new Pawn(
                manager.get(ChessPiecesTexturesPaths[ChessPiecesPaths.W_PAWN.ordinal()],Texture.class),
                E2.getPosition().getX(),
                E2.getPosition().getY()
        );
        whiteChesses[ChessPiecesInArray.Pawn6.ordinal()] = new Pawn(
                manager.get(ChessPiecesTexturesPaths[ChessPiecesPaths.W_PAWN.ordinal()],Texture.class),
                F2.getPosition().getX(),
                F2.getPosition().getY()
        );
        whiteChesses[ChessPiecesInArray.Pawn7.ordinal()] = new Pawn(
                manager.get(ChessPiecesTexturesPaths[ChessPiecesPaths.W_PAWN.ordinal()],Texture.class),
                G2.getPosition().getX(),
                G2.getPosition().getY()
        );
        whiteChesses[ChessPiecesInArray.Pawn8.ordinal()] = new Pawn(
                manager.get(ChessPiecesTexturesPaths[ChessPiecesPaths.W_PAWN.ordinal()],Texture.class),
                H2.getPosition().getX(),
                H2.getPosition().getY()
        );

        ////////////////////////////////////////
        blackChesses[ChessPiecesInArray.King.ordinal()] = new King(
                manager.get(ChessPiecesTexturesPaths[ChessPiecesPaths.B_KING.ordinal()],Texture.class),
                E8.getPosition().getX(),
                E8.getPosition().getY()
        );

        blackChesses[ChessPiecesInArray.Queen.ordinal()] = new Queen(
                manager.get(ChessPiecesTexturesPaths[ChessPiecesPaths.B_QUEEN.ordinal()],Texture.class),
                D8.getPosition().getX(),
                D8.getPosition().getY()
        );

        blackChesses[ChessPiecesInArray.FstBishop.ordinal()] = new Bishop(
                manager.get(ChessPiecesTexturesPaths[ChessPiecesPaths.B_BISHOP.ordinal()],Texture.class),
                C8.getPosition().getX(),
                C8.getPosition().getY()
        );

        blackChesses[ChessPiecesInArray.SndBishop.ordinal()] = new Bishop(
                manager.get(ChessPiecesTexturesPaths[ChessPiecesPaths.B_BISHOP.ordinal()],Texture.class),
                F8.getPosition().getX(),
                F8.getPosition().getY()
        );

        blackChesses[ChessPiecesInArray.FstKnight.ordinal()] = new Knight(
                manager.get(ChessPiecesTexturesPaths[ChessPiecesPaths.B_KNIGHT.ordinal()],Texture.class),
                B8.getPosition().getX(),
                B8.getPosition().getY()
        );

        blackChesses[ChessPiecesInArray.SndKnight.ordinal()] = new Knight(
                manager.get(ChessPiecesTexturesPaths[ChessPiecesPaths.B_KNIGHT.ordinal()],Texture.class),
                G8.getPosition().getX(),
                G8.getPosition().getY()
        );

        blackChesses[ChessPiecesInArray.FstRook.ordinal()] = new Rook(
                manager.get(ChessPiecesTexturesPaths[ChessPiecesPaths.B_ROOK.ordinal()],Texture.class),
                A8.getPosition().getX(),
                A8.getPosition().getY()
        );

        blackChesses[ChessPiecesInArray.SndRook.ordinal()] = new Rook(
                manager.get(ChessPiecesTexturesPaths[ChessPiecesPaths.B_ROOK.ordinal()],Texture.class),
                H8.getPosition().getX(),
                H8.getPosition().getY()
        );

        blackChesses[ChessPiecesInArray.Pawn1.ordinal()] = new Pawn(
                manager.get(ChessPiecesTexturesPaths[ChessPiecesPaths.B_PAWN.ordinal()],Texture.class),
                A7.getPosition().getX(),
                A7.getPosition().getY()
        );
        blackChesses[ChessPiecesInArray.Pawn2.ordinal()] = new Pawn(
                manager.get(ChessPiecesTexturesPaths[ChessPiecesPaths.B_PAWN.ordinal()],Texture.class),
                B7.getPosition().getX(),
                B7.getPosition().getY()
        );
        blackChesses[ChessPiecesInArray.Pawn3.ordinal()] = new Pawn(
                manager.get(ChessPiecesTexturesPaths[ChessPiecesPaths.B_PAWN.ordinal()],Texture.class),
                C7.getPosition().getX(),
                C7.getPosition().getY()
        );
        blackChesses[ChessPiecesInArray.Pawn4.ordinal()] = new Pawn(
                manager.get(ChessPiecesTexturesPaths[ChessPiecesPaths.B_PAWN.ordinal()],Texture.class),
                D7.getPosition().getX(),
                D7.getPosition().getY()
        );
        blackChesses[ChessPiecesInArray.Pawn5.ordinal()] = new Pawn(
                manager.get(ChessPiecesTexturesPaths[ChessPiecesPaths.B_PAWN.ordinal()],Texture.class),
                E7.getPosition().getX(),
                E7.getPosition().getY()
        );
        blackChesses[ChessPiecesInArray.Pawn6.ordinal()] = new Pawn(
                manager.get(ChessPiecesTexturesPaths[ChessPiecesPaths.B_PAWN.ordinal()],Texture.class),
                F7.getPosition().getX(),
                F7.getPosition().getY()
        );
        blackChesses[ChessPiecesInArray.Pawn7.ordinal()] = new Pawn(
                manager.get(ChessPiecesTexturesPaths[ChessPiecesPaths.B_PAWN.ordinal()],Texture.class),
                G7.getPosition().getX(),
                G7.getPosition().getY()
        );
        blackChesses[ChessPiecesInArray.Pawn8.ordinal()] = new Pawn(
                manager.get(ChessPiecesTexturesPaths[ChessPiecesPaths.B_PAWN.ordinal()],Texture.class),
                H7.getPosition().getX(),
                H7.getPosition().getY()
        );

        PlayerOne.setPlayerName("TemplateName");

        if (computerEnemy) {
            PlayerOne.setPlayerName("Bot Clark");
        } else {
            PlayerTwo.setPlayerName("TemplateName");
        }

        int xHot = crosshairPixmaps[0].getWidth() / 2;
        int yHot = crosshairPixmaps[0].getHeight() / 2;
        crosshairs[0] = Gdx.graphics.newCursor(crosshairPixmaps[0], xHot, yHot);
        xHot = crosshairPixmaps[1].getWidth() / 2;
        yHot = crosshairPixmaps[1].getHeight() / 2;
        crosshairs[1] = Gdx.graphics.newCursor(crosshairPixmaps[1], xHot, yHot);
        xHot = 0;
        yHot = 0;
        crosshairs[2] = Gdx.graphics.newCursor(crosshairPixmaps[2], xHot, yHot);

        Gdx.graphics.setCursor(crosshairs[2]);

        return true;
    }

    /**
     * Metoda do określania na który szach kliknięto
     * myszki
     *
     * @param screenX Pozycja x na ekranie
     * @param screenY Pozycja y na ekranie
     */
    // Stage 3 methods to click on a chess
    protected void touchDownSprite(int screenX, int screenY) {
//        for (int i = 0; i < sum; i++) {
//            if (FirstBoardShipsSprites[i].spriteContains(new Vector2(screenX, gameHeight_f - screenY))) {
//                activeSpriteDrag = i;
//            }
//        }
    }

    /**
     * Metoda do poruszania spriteów na planszy click n drop
     *
     * @param screenX Nowa pozycja X na ekranie
     * @param screenY Nowa pozycja Y na ekranie
     */
    protected void moveChess(int screenX, int screenY, Chess chess) {

    }

    /**
     * Metoda do rysowania tekstu pomocy w czasie przed bitwą
     *
     * @param font  Czcionka do tekstu
     * @param batch SpriteBatch do rysowania na ekranie
     */
    protected void drawStage2Text(BitmapFont font, SpriteBatch batch) {
        String text = "Lets draw who starts first!";
        int len = text.length();
        font.draw(batch, text, (gameWidth_f - 200 - (43 * (len / 2))), gameHeight_f / 2 + 200);
        text = "Confirm it by clicking READY button !";
        len = text.length();
        font.draw(batch, text, (gameWidth_f - 180 - (43 * (len / 2))), gameHeight_f / 2 + 100);
    }


    /**
     * Metoda do zwalniania zasobów wykorzystywanych przez klasę
     */
    @Override
    public void dispose() {
        super.dispose();
    }
}
