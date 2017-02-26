package ovh.exception.watchdogzz.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ovh.exception.watchdogzz.R;
import ovh.exception.watchdogzz.data.User;
import ovh.exception.watchdogzz.data.UserManager;
import ovh.exception.watchdogzz.view.WDRenderer;
import ovh.exception.watchdogzz.view.WDSurfaceView;

/**
 * A fragment representing a single User detail screen.
 * This fragment is either contained in a {@link UserListActivity}
 * in two-pane mode (on tablets) or a {@link UserDetailActivity}
 * on handsets.
 */
public class UserDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";
    public static final String ARG_ITEM = "item";

    /**
     * The dummy content this fragment is presenting.
     */
    private User mItem;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public UserDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (null==savedInstanceState && getArguments().containsKey(ARG_ITEM)) {
            mItem = getArguments().getParcelable(ARG_ITEM);

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mItem.getName());
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.user_detail, container, false);

        WDSurfaceView glView = (WDSurfaceView) rootView.findViewById(R.id.user_map);
        WDRenderer renderer = new WDRenderer(getActivity());
        glView.setRenderer(renderer);
        UserManager um = new UserManager();
        um.addObserver(renderer.getMap());

        if (mItem != null) {
            //((TextView) rootView.findViewById(R.id.user_detail)).setText(mItem.getPosition().toString());
            um.addUser(mItem);
        }

        return rootView;
    }

}
