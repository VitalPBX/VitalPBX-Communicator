package org.vpbxcommunicator.utils;

/*
ImageUtils.java
Copyright (C) 2018  Belledonne Communications, Grenoble, France

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
*/

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import org.vpbxcommunicator.R;

public class ImageUtils {

    public static Bitmap getRoundBitmapFromUri(Context context, Uri fromPictureUri) {
        Bitmap bm;
        Bitmap roundBm;
        if (fromPictureUri != null) {
            try {
                bm =
                        MediaStore.Images.Media.getBitmap(
                                context.getContentResolver(), fromPictureUri);
            } catch (Exception e) {
                bm = BitmapFactory.decodeResource(context.getResources(), R.drawable.contact);
            }
        } else {
            // bm = BitmapFactory.decodeResource(context.getResources(), R.drawable.contact);
            Drawable contactDrawable = context.getResources().getDrawable(R.drawable.contact);
            contactDrawable.setTint(context.getResources().getColor(R.color.grey_color));

            bm = drawableToBitmap(contactDrawable);
        }
        if (bm != null) {
            roundBm = getRoundBitmap(bm);
            if (roundBm != null) {
                bm.recycle();
                bm = roundBm;
            }
        }
        return bm;
    }

    private static Bitmap getRoundBitmap(Bitmap bitmap) {
        Bitmap output =
                Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawCircle(
                bitmap.getWidth() / 2, bitmap.getHeight() / 2, bitmap.getWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    public static float dpToPixels(Context context, float dp) {
        return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }

    public static float pixelsToDp(Context context, float pixels) {
        return pixels
                / ((float) context.getResources().getDisplayMetrics().densityDpi
                        / DisplayMetrics.DENSITY_DEFAULT);
    }

    /**
     * used to convert contact icon (drawable resource) into bitmap that way it can be used to put
     * it in a notification
     */
    public static Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        Bitmap bitmap =
                Bitmap.createBitmap(
                        drawable.getIntrinsicWidth(),
                        drawable.getIntrinsicHeight(),
                        Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }
}
