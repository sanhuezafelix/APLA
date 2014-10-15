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
public class mDownloadContentViewController extends Fragment{

    private static final String EVENTO_WWW = "evento_www";


    private WebView www;





    public static mDownloadContentViewController newInstance(MainActivity a ,String www){
        mDownloadContentViewController fragment = new mDownloadContentViewController();
        a.setHideNavigationBar();
        Bundle bundle = new Bundle();

        bundle.putString(EVENTO_WWW, www);

        fragment.setArguments(bundle);
        fragment.setRetainInstance(true);
        return fragment;
    }
    private  String LinkToPDF ;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View RootView = inflater.inflate(R.layout.libro_resumen_layout,container,false);
        ((MainActivity)getActivity()).setHideNavigationBar();
        ((MainActivity)getActivity()).setVisibilityChangeViewChat(View.GONE);
        www = (WebView)RootView.findViewById(R.id._webview);

        www.getSettings().setJavaScriptEnabled(true);
        this.LinkToPDF  = getArguments().getString(EVENTO_WWW).toString();


        www.loadUrl( LinkToPDF);

        return RootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity)activity).setHideNavigationBar();
        ((MainActivity)activity).setTituloVista(getString(R.string.content));
        ((MainActivity)getActivity()).setVisibilityChangeViewChat(View.GONE);
    }
}