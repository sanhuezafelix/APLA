package Fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import mc.mCongressDemo.MainActivity;
import mc.mCongressDemo.R;

/**
 * Created by luisgonzalez on 22-04-14.
 */
public class mLibroResumenViewController  extends Fragment{

    private WebView www;




    public static mLibroResumenViewController newInstance(MainActivity a){
        mLibroResumenViewController fragment = new mLibroResumenViewController();
        a.setHideNavigationBar();
        return fragment;
    }
    private final String LinkToPDFes = "http://croxz.com/mobi/Resumenes.pdf";
    private final String LinkToPDFen = "http://croxz.com/mobi/Resumenes.pdf";
    private final String LinkToPDFpt = "http://croxz.com/mobi/Resumenes.pdf";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View RootView = inflater.inflate(R.layout.libro_resumen_layout,container,false);
        ((MainActivity)getActivity()).setHideNavigationBar();
        www = (WebView)RootView.findViewById(R.id._webview);

        www.getSettings().setJavaScriptEnabled(true);
        if (getString(R.string.idioma).equals("es")){
            www.loadUrl("https://docs.google.com/gview?embedded=true&url=" + LinkToPDFes);
            Log.i("pdf", "español");

        }
        else if(getString(R.string.idioma).equals("pt"))
        {
            www.loadUrl("https://docs.google.com/gview?embedded=true&url=" + LinkToPDFpt);
            Log.i("pdf","portugues");
        }
        else {
            www.loadUrl("https://docs.google.com/gview?embedded=true&url=" + LinkToPDFen);

            Log.i("pdf","ingles");
        }



        return RootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity)activity).setHideNavigationBar();
        ((MainActivity)activity).setTituloVista(getString(R.string.Summary_Book));
    }
}
