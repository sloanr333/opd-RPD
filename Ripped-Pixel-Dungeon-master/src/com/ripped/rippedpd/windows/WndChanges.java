package com.ripped.rippedpd.windows;

import com.ripped.rippedpd.scenes.PixelScene;
import com.ripped.rippedpd.ui.ScrollPane;
import com.ripped.rippedpd.ui.Window;
import com.watabou.noosa.BitmapText;
import com.watabou.noosa.BitmapTextMultiline;
import com.watabou.noosa.ui.Component;

public class WndChanges extends Window {

    private static final int WIDTH	= 112;
    private static final int HEIGHT	= 160;

    private static final String TXT_TITLE	= "Changes";

    private static final String TXT_CHANGES	= "test test test test test test test test test test test test test test test" +
            "\n\n test test test test test test test test test test test test test test test\ntest test test test " +
            "test test test test test test\n\n\ntest test test test testtest test test test test";

    private BitmapText txtTitle;
    private ScrollPane text;

    public WndChanges() {
        super();
        resize( WIDTH, HEIGHT );

        txtTitle = PixelScene.createText(TXT_TITLE, 9);
        txtTitle.hardlight( Window.SHPX_COLOR );
        txtTitle.measure();
        txtTitle.x = PixelScene.align( PixelScene.uiCamera, (WIDTH - txtTitle.width()) / 2 );
        add( txtTitle );

        BitmapTextMultiline txtChanges = PixelScene.createMultiline(TXT_CHANGES, 9);

        Component content = new Component();

        content.add(txtChanges);

        text = new ScrollPane( content ) {

        };
        add( text );
        text.setRect( 0, txtTitle.height(), WIDTH, HEIGHT - txtTitle.height() );
    }
}

