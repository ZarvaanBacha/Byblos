package Utilities;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Pattern;

public class Utilities {

  final static String TAG = "Utilities";
  final static Character[] INVALID_CHARS = { ' ', '!', '"', '#', '$', '%', '&', '\'', '(', ')', '*', '+', ',', '-', '.', '/', ':',
          ';', '<', '=', '>', '?', '@', '[', '\\', ']', '^', '_', '`', '{', '|', '}', '~' };

  public static void toastCancel( Toast toast ) {
    Handler handler = new Handler();
    handler.postDelayed( toast::cancel, 1000 );
  }

  /**
   * @param textStr name string which shouldn't contain characters like _*()/> etc.
   * @return true if is a valid name
   */
  public static boolean nameChecker( String textStr ) {

    if ( textStr.equals( "" ) ) return false;
    return stringChecker( textStr, INVALID_CHARS );
  }

  public static boolean generalTextChecker( String textStr ) {

    if ( textStr.equals( "" ) ) return false;

    Character[] INVALID_CHARS = { '!', '"', '#', '$', '%', '&', '\'', '*', '/',
            '<', '=', '>', '?', '@', '\\', '^', '`', '{', '|', '}', '~' };
    return Utilities.stringChecker( textStr, INVALID_CHARS );
  }

  /**
   * An simple black-list checker for human names.
   *
   * @param email full email string, i.e. "apple_is.bad@email.com"
   * @return true if is a valid string without ASCII non alphabetic characters
   */
  public static boolean emailChecker( String email ) {

    if ( email.equals( "" ) ) return false;
    if ( !email.contains( "@" ) ) return false;

    String regexPattern = "^[a-zA-Z0-9_]+(?:\\.[a-zA-Z0-9_]+)*@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$";
    Pattern pattern = Pattern.compile( regexPattern );
    return pattern.matcher( email ).matches();
  }

  private static boolean numberChecker( String textStr, int totalPositions ) {

    if ( textStr.equals( "" ) ) return false;
    boolean isValid = stringChecker( textStr, INVALID_CHARS );
    isValid = isValid && ( textStr.length() == totalPositions );

    return isValid;
  }

  private static boolean timeStrChecker( String textStr ) {

    if ( textStr.equals( "" ) ) return false;
    boolean isValid = numberChecker( textStr, 2 );

    try {
      int hour = Integer.parseInt( textStr );
      isValid = isValid && ( hour <= 23 ) && ( hour >= 0 );
    } catch ( NumberFormatException e ) {
      return false;
    }

    return isValid;
  }

  private static boolean dateStrChecker( String textStr ) {

    if ( textStr.length() != 10 ) return false;
    SimpleDateFormat dateFormat = new SimpleDateFormat( "dd/MM/yyyy", new Locale( "en" ) );
    try {
      Date date = dateFormat.parse( textStr );
      assert date != null;
    } catch ( ParseException e ) {
      return false;
    }

    return true;
  }

  /**
   * An simple black-list checker for human names.
   *
   * @return true if is a valid string without ASCII non alphabetic characters
   */
  public static boolean stringChecker( String str, Character[] invalidChars ) {

    for ( Character c : str.toCharArray() ) {
      for ( Character invalidC : invalidChars )
        if ( c.equals( invalidC ) ) {

          Log.d( TAG, "-------- invalid chars -------- " + str );
          return false;
        }
    }
    return true;
  }

  /**
   * @param message the Toast message to be displayed
   * @param context the Toast context
   */
  public static boolean validateTextView_Password( TextView textView, String text, String message, Context context ) {

    boolean isValid = (text.length() >= 6);

    if ( !isValid ) {
      final Toast toast = Toast.makeText( context, message, Toast.LENGTH_SHORT );
      toast.show();
      textView.setTextColor( Color.RED );
      toastCancel( toast );
      return false;
    }
    textView.setTextColor( Color.BLACK );
    return true;
  }

  /* --------- Validators for Car/Truck Forms ----------- */

  /**
   * @param message the Toast message to be displayed
   * @param context the Toast context
   */
  public static boolean validateTextView_General( TextView textView, String text, String message, Context context ) {
    boolean isValid = Utilities.nameChecker( text );
    if ( !isValid ) {
      final Toast toast = Toast.makeText( context, message, Toast.LENGTH_SHORT );
      toast.show();
      textView.setTextColor( Color.RED );
      toastCancel( toast );
      return false;
    }
    textView.setTextColor( Color.BLACK );
    return true;
  }

  /**
   * @param message the Toast message to be displayed
   * @param context the Toast context
   */
  public static boolean validateTextView_Address( TextView textView, String text, String message, Context context ) {
    Character[] INVALID_CHARS = { '!', '"', '#', '$', '%', '&', '\'', '*', '+', '.', '/', ':',
            ';', '<', '=', '>', '?', '@', '\\', '^', '`', '{', '|', '}', '~' };
    boolean isValid = Utilities.stringChecker( text, INVALID_CHARS );
    if ( !isValid ) {
      final Toast toast = Toast.makeText( context, message, Toast.LENGTH_SHORT );
      toast.show();
      textView.setTextColor( Color.RED );
      toastCancel( toast );
      return false;
    }
    textView.setTextColor( Color.BLACK );
    return true;
  }

  /**
   * @param message the Toast message to be displayed
   * @param context the Toast context
   */
  public static boolean validateTextView_Email( TextView textView, String text, String message, Context context ) {
    boolean isValid = Utilities.emailChecker( text );
    if ( !isValid ) {
      final Toast toast = Toast.makeText( context, message, Toast.LENGTH_SHORT );
      toast.show();
      textView.setTextColor( Color.RED );
      toastCancel( toast );
      return false;
    }
    textView.setTextColor( Color.BLACK );
    return true;
  }

  /**
   * @param message the Toast message to be displayed
   * @param context the Toast context
   */
  public static boolean validateTextView_Time( TextView textView, String text, String message, Context context ) {
    boolean isValid = Utilities.timeStrChecker( text );
    if ( !isValid ) {
      final Toast toast = Toast.makeText( context, message, Toast.LENGTH_SHORT );
      toast.show();
      textView.setTextColor( Color.RED );
      toastCancel( toast );
      return false;
    }
    textView.setTextColor( Color.BLACK );
    return true;
  }

  /**
   * @param message the Toast message to be displayed
   * @param context the Toast context
   */
  public static boolean validateTextView_DriverLicense( TextView textView, String text, String message, Context context ) {
    boolean isValid;

    switch ( text ) {
      case "G":
      case "G1":
      case "G2":
        isValid = true;
        break;
      default:
        isValid = false;
    }

    if ( !isValid ) {
      final Toast toast = Toast.makeText( context, message, Toast.LENGTH_SHORT );
      toast.show();
      textView.setTextColor( Color.RED );
      toastCancel( toast );
      return false;
    }
    textView.setTextColor( Color.BLACK );
    return true;
  }

  /**
   * @param message the Toast message to be displayed
   * @param context the Toast context
   */
  public static boolean validateTextView_DateOfBirth( TextView textView, String text, String message, Context context ) {
    boolean isValid = Utilities.dateStrChecker( text );

    if ( !isValid ) {
      final Toast toast = Toast.makeText( context, message, Toast.LENGTH_SHORT );
      toast.show();
      textView.setTextColor( Color.RED );
      toastCancel( toast );
      return false;
    }
    textView.setTextColor( Color.BLACK );
    return true;
  }

  /* --------- Validators for Moving Forms ----------- */

  /**
   * @param message the Toast message to be displayed
   * @param context the Toast context
   */
  public static boolean validateTextView_Boxes( TextView textView, String text, String message, Context context ) {
    boolean isValid = Utilities.nameChecker( text );

    if ( isValid ) {
      try {
        int num = Integer.parseInt( text );
        isValid = ( num >= 1 && num <= 99 );
      } catch ( NumberFormatException e ) {
        isValid = false;
      }
    }

    if ( !isValid ) {
      final Toast toast = Toast.makeText( context, message, Toast.LENGTH_SHORT );
      toast.show();
      textView.setTextColor( Color.RED );
      toastCancel( toast );
      return false;
    }
    textView.setTextColor( Color.BLACK );
    return true;
  }

  /* --------- Validators for Admin ----------- */

  /**
   * @param message the Toast message to be displayed
   * @param context the Toast context
   */
  public static boolean validateTextView_CarNames( TextView textView, String text, String message, Context context ) {
    boolean isValid = Utilities.generalTextChecker( text );

    if ( !isValid ) {
      final Toast toast = Toast.makeText( context, message, Toast.LENGTH_SHORT );
      toast.show();
      textView.setTextColor( Color.RED );
      toastCancel( toast );
      return false;
    }
    textView.setTextColor( Color.BLACK );
    return true;
  }

  /**
   * @param message the Toast message to be displayed
   * @param context the Toast context
   */
  public static boolean validateTextView_Price( TextView textView, String text, String message, Context context ) {
    boolean isValid = Utilities.generalTextChecker( text );

    if ( isValid ) {
      try {
        double num = Double.parseDouble( text );
        isValid = ( num >= 0f );
      } catch ( NumberFormatException e ) {
        isValid = false;
      }
    }

    if ( !isValid ) {
      final Toast toast = Toast.makeText( context, message, Toast.LENGTH_SHORT );
      toast.show();
      textView.setTextColor( Color.RED );
      toastCancel( toast );
      return false;
    }
    textView.setTextColor( Color.BLACK );
    return true;
  }

}
