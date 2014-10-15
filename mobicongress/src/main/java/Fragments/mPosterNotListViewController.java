package Fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import mc.mCongressDemo.MainActivity;
import mc.mCongressDemo.R;

/**
 * Created by luisgonzalez on 22-04-14.
 */
public class mPosterNotListViewController extends Fragment{

    private WebView www;
    private static final String LinkToPDF = "linkTopdf";




    public static mPosterNotListViewController newInstance(MainActivity a, String link){
        mPosterNotListViewController fragment = new mPosterNotListViewController();
        Bundle bundle = new Bundle();
        bundle.putString(LinkToPDF, link);
        fragment.setArguments(bundle);
        fragment.setRetainInstance(true);
        a.setHideNavigationBar();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View RootView = inflater.inflate(R.layout.libro_resumen_layout,container,false);
        ((MainActivity)getActivity()).setHideNavigationBar();
        www = (WebView)RootView.findViewById(R.id._webview);

        www.getSettings().setJavaScriptEnabled(true);


        www.loadUrl("https://docs.google.com/gview?embedded=true&url=" + getArguments().getString(LinkToPDF));


        return RootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity)activity).setHideNavigationBar();
        ((MainActivity)activity).setTituloVista(getString(R.string.Summary_Book));
    }
}
