package ua.meugen.android.levelup.restaurantsmap.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;

import ua.meugen.android.levelup.restaurantsmap.R;

public final class ConnectionErrorFragment extends Fragment {

    private static final String RESULT_KEY = "result";

    public static ConnectionErrorFragment newInstance(final ConnectionResult result) {
        final Bundle arguments = new Bundle();
        arguments.putParcelable(RESULT_KEY, result);

        final ConnectionErrorFragment fragment = new ConnectionErrorFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    private ConnectionResult result;

    @Nullable
    @Override
    public View onCreateView(
            final LayoutInflater inflater,
            @Nullable final ViewGroup container,
            @Nullable final Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            restoreInstanceState(getArguments());
        } else {
            restoreInstanceState(savedInstanceState);
        }

        final View view = inflater.inflate(R.layout.fragment_connection_error, container, false);
        final TextView codeView = (TextView) view.findViewById(R.id.code);
        codeView.setText(Integer.toString(this.result.getErrorCode()));
        final TextView messageView = (TextView) view.findViewById(R.id.message);
        messageView.setText(this.result.getErrorMessage());
        return view;
    }

    private void restoreInstanceState(final Bundle instanceState) {
        this.result = instanceState.getParcelable(RESULT_KEY);
    }

    @Override
    public void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(RESULT_KEY, this.result);
    }
}
