package estacio.br.com.procelula.Components;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Barque on 20/05/2017.
 */

public class TitilliumWebBoldTextView extends TextView {
    public TitilliumWebBoldTextView(Context context) {
        super(context);
    }

    public TitilliumWebBoldTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TitilliumWebBoldTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    private void applyCustomFont(Context context) {
        Typeface customFont = FontCache.getTypeface("fonts/TitilliumWeb-Bold.ttf", context);
        setTypeface(customFont);
    }
}
