package com.mm.mobilemechanic.util;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Cipher;

public class Utility {


    public static ProgressDialog mProgressDialog;
    private static ProgressDialog loadingDialog;
    private static Dialog mDialog;

    public static void showToast(String msg, Context context) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }


    public static String strCapitalize(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }





    public static Bitmap getBitmap(Bitmap bitmap, String path) {

        ExifInterface ei = null;
        try {
            ei = new ExifInterface(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_UNDEFINED);
        Bitmap final_bitmap = null;
        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                final_bitmap = rotateImage(bitmap, 90);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                final_bitmap = rotateImage(bitmap, 180);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                final_bitmap = rotateImage(bitmap, 270);
                break;
            case ExifInterface.ORIENTATION_NORMAL:
            default:
                final_bitmap = bitmap;
                break;
        }
        return final_bitmap;
    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix,
                true);
    }

    public static boolean isNetworkAvailable(Context _context) {
        ConnectivityManager connectivity = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
        }
        return false;
    }

    public static boolean emailCheck(String email) {
        try {
            Pattern pattern = Pattern.compile("^([a-z0-9\\+_\\-]+)(\\.[a-z0-9\\+_\\-]+)*@([a-z0-9\\-]+\\.)+[a-z]{2,6}$");
            Matcher matcher = pattern.matcher(email);
            return matcher.matches();
        } catch (Exception ex) {
            return false;
        }
    }

    public static boolean passwordCheck(String password) {
        try {
            if (password.length() >= 6 && password.length() <= 12)
                return true;
            return false;
        } catch (Exception ex) {
            return false;
        }
    }

    public static boolean confirmpasswordCheck(String confirmpassword, String password) {
        try {
            if (confirmpassword.equals(password))
                return true;
            return false;
        } catch (Exception ex) {
            return false;
        }
    }

    public static void showSimpleProgressDialog(Context context) {
        showSimpleProgressDialog(context, null, "Loading...", false);
    }

    public static void showSimpleProgressDialog(Context context, String title,
                                                String msg, boolean isCancelable) {
        try {
            if (mProgressDialog == null) {
                mProgressDialog = ProgressDialog.show(context, title, msg);
                mProgressDialog.setCancelable(isCancelable);
            }
            if (!mProgressDialog.isShowing()) {
                mProgressDialog.show();
            }
        } catch (IllegalArgumentException ie) {
            ie.printStackTrace();
        } catch (RuntimeException re) {
            re.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void removeSimpleProgressDialog() {
        try {
            if (mProgressDialog != null) {
                if (mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                    mProgressDialog = null;
                }
            }
        } catch (IllegalArgumentException ie) {
            ie.printStackTrace();
        } catch (RuntimeException re) {
            re.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void hideKeyboard(Activity activity) {
        if (activity != null && activity.getWindow() != null && activity.getWindow().getDecorView() != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), 0);
        }
    }

    public static void setupUI(View view, final Activity mActivity) {

        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(mActivity);
                    return false;
                }
            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUI(innerView, mActivity);
            }
        }
    }

    public static void ChangeNavTopColor(Activity con) {
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = con.getWindow();
            try {
                //window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                //	window.setStatusBarColor(con.getResources().getColor(R.color.colorPrimary));
            } catch (Exception e) {
                // upgrade your SDK and ADT :D
            }
        }
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    public static boolean is_blank(EditText edt_temp) {
        if (edt_temp != null) {
            if (edt_temp.getText().toString().trim().length() == 0)
                return true;
            return false;
        }
        return false;
    }

    public static void display_error(EditText edt_temp, String error) {
        if (error != null && edt_temp != null) {
            edt_temp.setError(error);
            edt_temp.requestFocus();
        }
    }

    public static void keyboard_down_outside_touch(View view, final Activity activity) {
        //Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {

                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(activity);
                    return false;
                }

            });
        }
        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                keyboard_down_outside_touch(innerView, activity);
            }
        }
    }

    public static String calculateCurrentWeek() {

        Calendar now = Calendar.getInstance();
        Log.e("DDDDDDDDDDD", now.get(Calendar.WEEK_OF_YEAR) + "");
        return now.get(Calendar.WEEK_OF_YEAR) + "";

    }

    public static String calculateCurrentYear() {

        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        calendar.get(Calendar.YEAR);
        Log.e("YEARRRRR", calendar.get(Calendar.YEAR) + "");
        String year = calendar.get(Calendar.YEAR) + "";
        Log.e("EEEEEEEEEEEE", year.substring(2, year.length()));
        return year.substring(2, year.length());
    }

    public static String calculateWholeCurrentYear() {

        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        calendar.get(Calendar.YEAR);
        Log.e("YEARRRRR", calendar.get(Calendar.YEAR) + "");
        String year = calendar.get(Calendar.YEAR) + "";

        return year;
    }

    public static String calculateMon() {

        Calendar cal = Calendar.getInstance();
        return (new SimpleDateFormat("MMM").format(cal.getTime()));

    }

    public static String calculateMonth() {

        Calendar cal = Calendar.getInstance();
        return (new SimpleDateFormat("MM").format(cal.getTime()));

    }

    public static String convertDateMMYYYY(String date) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date myDate = null;
        try {
            myDate = df.parse(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat dfor = new SimpleDateFormat("MMM-yyyy");
        String finalDate = dfor.format(myDate);
        return finalDate;
    }



    public static String perfectDecimal(String str, int MAX_BEFORE_POINT, int MAX_DECIMAL) {
        if (str.charAt(0) == '.') str = "0" + str;
        int max = str.length();

        String rFinal = "";
        boolean after = false;
        int i = 0, up = 0, decimal = 0;
        char t;
        while (i < max) {
            t = str.charAt(i);
            if (t != '.' && after == false) {
                up++;
                if (up > MAX_BEFORE_POINT) return rFinal;
            } else if (t == '.') {
                after = true;
            } else {
                decimal++;
                if (decimal > MAX_DECIMAL)
                    return rFinal;
            }
            rFinal = rFinal + t;
            i++;
        }
        return rFinal;
    }

    public static boolean checkInternalUser(Context context) {

        SharedPreferences prefs = context.getSharedPreferences("ceat", 0);
        String loginurl = prefs.getString("loginurl", "");
        String ProfileName__c = prefs.getString("ProfileName__c", "");

        if (loginurl.contains("salesforce") && (ProfileName__c.equalsIgnoreCase("Replacements") || ProfileName__c.equalsIgnoreCase("Replacements Temp"))) {
            return true;
        } else {
            return false;
        }

    }

    public static String convertDate(String date) {

        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        Date myDate = null;
        try {
            myDate = df.parse(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat dfor = new SimpleDateFormat("yyyy-MM-dd");
        String finalDate = dfor.format(myDate);
        return finalDate;
    }

    public static void hideProgressDialog() {
        try {
            if (loadingDialog != null && loadingDialog.isShowing())
                loadingDialog.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ProgressDialog showProgressDialog(Context context, String mMessage) {
        hideProgressDialog();
        loadingDialog = new ProgressDialog(context);
        loadingDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        loadingDialog.setMessage(mMessage);
        loadingDialog.setIndeterminate(false);
        loadingDialog.setCancelable(false);
        return loadingDialog;
    }


    public static boolean checkValidMobile(Context context, String mobilestr) {
        if (mobilestr.equals("")) {
            //Toast.makeText(context, "Please enter Mobile Number", Toast.LENGTH_LONG).show();
            return false;
        } else if (mobilestr.length() != 10) {
            //  Toast.makeText(context, "Please enter 10 digit Customer Mobile Number", Toast.LENGTH_LONG).show();
            return false;
        } else {
            return true;
        }
    }

    public static PrivateKey getPrivateKey(String privateString) {

        PrivateKey privateKey = null;

        try {
           // String privateString = getString(key, context);
            if(privateString!=null){
                byte[] binCpk = Base64.decode(privateString, Base64.DEFAULT);
                KeyFactory keyFactory = KeyFactory.getInstance("RSA","BC");
                PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(binCpk);
                privateKey = keyFactory.generatePrivate(privateKeySpec);

            }
        }
        catch(Exception e){
            String r="";
        }
        return privateKey;
    }

    public static String getCurrentDate() {

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate = df.format(c.getTime());
        return formattedDate;

    }


    public static String convertFileToByteArray(File f) {
      /*  Uri uri = Uri.fromFile(f);
        f = new File(uri.getPath());
*/
        byte[] byteArray = null;
        try {
            if (f != null) {
                InputStream inputStream = new FileInputStream(f);
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                byte[] b = new byte[1024 * 11];
                int bytesRead = 0;

                while ((bytesRead = inputStream.read(b)) != -1) {
                    bos.write(b, 0, bytesRead);
                }

                byteArray = bos.toByteArray();

                Log.e("Byte array", ">" + byteArray);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    public static double calculateEMI(double p, double n, double r) {
        r = calculateMonthlyROI(r);
        double rn = 1 + r;
        rn = Math.pow(rn, n);
        double rn_1 = rn - 1;
        double dividedval = rn / rn_1;

        double emi = p * r * dividedval;

        return emi;

    }

    public static double calculateMonthlyROI(double r) {

        return r / 12 / 100;

    }


    public static boolean validateLTV(double loanAmnt, double propFinancible) {

        if (loanAmnt != 0 && propFinancible != 0) {
            double LTV = loanAmnt / propFinancible;

            if (loanAmnt > 2000000 && LTV > 0.80) {
                return false;
            } else if (loanAmnt <= 2000000 && LTV > 0.90) {
                return false;
            } else {
                return true;
            }

        } else {
            return false;
        }


    }

    public static void displaypdf(String path, Context mContext) {

        File file = null;
        file = new File(path);
        // Toast.makeText(mContext, file.toString(), Toast.LENGTH_LONG).show();
        if (file.exists()) {
            Intent target = new Intent(Intent.ACTION_VIEW);
            target.setDataAndType(Uri.fromFile(file), "application/pdf");
            target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

            Intent intent = Intent.createChooser(target, "Open File");
            try {
                mContext.startActivity(intent);
            } catch (ActivityNotFoundException e) {
                // Instruct the user to install a PDF reader here, or something
            }
        } else
            Toast.makeText(mContext, "File path is incorrect.", Toast.LENGTH_LONG).show();
    }


}
