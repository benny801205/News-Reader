package androidsamples.java.journalapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;


public class EntryListFragment extends Fragment {
    private static final String TAG = "EntryListFragment";
    private EntryListViewModel mEntryListViewModel;
    Bundle bundle;
    boolean showFavoriteOnly= false;

    interface Callbacks {
        void onEntrySelected(UUID id);
    }

    private Callbacks mCallbacks = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bundle=getArguments();
        mEntryListViewModel = new ViewModelProvider(this).get(EntryListViewModel.class);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_entry_list, container, false);
       // FloatingActionButton fab = view.findViewById(R.id.floatingActionButton);
        //fab.setOnClickListener(this::FeedData);

        RecyclerView entriesList = view.findViewById(R.id.recyclerView);
        entriesList.setLayoutManager(new LinearLayoutManager(getActivity()));
        EntryListAdapter adapter = new EntryListAdapter(getActivity());
        entriesList.setAdapter(adapter);

        String order=bundle.getSerializable("sort").toString();

        if (showFavoriteOnly) {
            mEntryListViewModel.getFavoriteEntriesByTitle().observe(getActivity(), adapter::setEntries);
        }

        else {
            if (order.equalsIgnoreCase("publication date"))
                mEntryListViewModel.getEntriesByDate().observe(getActivity(), adapter::setEntries);
            else if (order.equalsIgnoreCase("visited"))
                mEntryListViewModel.getEntriesByVisited().observe(getActivity(), adapter::setEntries);
            else if (order.equalsIgnoreCase("favorite"))
                mEntryListViewModel.getEntriesByFavorite().observe(getActivity(), adapter::setEntries);
            else
                mEntryListViewModel.getEntriesByTitle().observe(getActivity(), adapter::setEntries);

        }



        return view;
    }




    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mCallbacks = (Callbacks) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {

        //menu.getItem(0).setIcon(android.R.drawable.alert_light_frame);

        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_entry_list, menu);



    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_set) {
              Fragment fragment = new SettingsFragment();
            fragment.setArguments(bundle);

            getParentFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
            return true;
        }
        else if(item.getItemId() == R.id.menu_fav)
        {
            if(showFavoriteOnly){
                showFavoriteOnly=false;

            }
            else {
                showFavoriteOnly=true;

            }

            FragmentTransaction ft = getParentFragmentManager().beginTransaction();
            ft.setReorderingAllowed(false);
            ft.detach(this).attach(this).commit();

        }




        return super.onOptionsItemSelected(item);
    }

    private class EntryViewHolder extends RecyclerView.ViewHolder {
        private final TextView mTxtTitle;
        private final TextView mTxtDate;
        private final ImageView mVisited;
        private JournalEntry mEntry;
        private ImageView mFavorite;

        public EntryViewHolder(@NonNull View itemView) {
            super(itemView);

            mTxtTitle = itemView.findViewById(R.id.txt_item_title);
            mTxtDate = itemView.findViewById(R.id.txt_item_date);
            mVisited=itemView.findViewById(R.id.icon_visited);
            mFavorite=itemView.findViewById(R.id.icon_fav);
            mFavorite.setOnClickListener(this::LaunchStar);

            itemView.setOnClickListener(this::launchJournalEntryFragment);
        }

        private void LaunchStar(View v){
            if(mEntry.favorite().equalsIgnoreCase("true")){
            //unsubscribe
                mEntry.setFavorate("false");
                mEntryListViewModel.saveEntry(mEntry);
                mFavorite.setImageResource(android.R.drawable.btn_star_big_off);

            }
            else{
            //subscribe
                mEntry.setFavorate("true");
                mEntryListViewModel.saveEntry(mEntry);
                mFavorite.setImageResource(android.R.drawable.btn_star_big_on);


            }

        }


        private void launchJournalEntryFragment(View v) {
            Log.d(TAG, "launchJournalEntryFragment with Entry: " + mEntry.title());
            mEntry.setVisited("true");
            mEntryListViewModel.saveEntry(mEntry);

            Fragment fragment = new WebFragment();
            Bundle args = new Bundle();
            args.putSerializable("url",mEntry.link());
            fragment.setArguments(args);

            getParentFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();




        }

        void bind(JournalEntry entry) {
            mEntry = entry;
            this.mTxtTitle.setText(mEntry.title());
            this.mTxtDate.setText(mEntry.date());
            //set visited icon
            if(mEntry.visited().equalsIgnoreCase("true"))
                this.mVisited.setVisibility(View.INVISIBLE);
            else
                this.mVisited.setVisibility(View.VISIBLE);
            //set favorite icon
            if(mEntry.favorite().equalsIgnoreCase("true"))
                mFavorite.setImageResource(android.R.drawable.btn_star_big_on);
            else
                mFavorite.setImageResource(android.R.drawable.btn_star_big_off);




        }
    }

    private class EntryListAdapter extends RecyclerView.Adapter<EntryViewHolder> {
        private final LayoutInflater mInflater;
        private List<JournalEntry> mEntries;

        public EntryListAdapter(Context context) {
            mInflater = LayoutInflater.from(context);
        }

        @NonNull
        @Override
        public EntryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = mInflater.inflate(R.layout.journal_item, parent, false);
            return new EntryViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull EntryViewHolder holder, int position) {
            if (mEntries != null) {
                JournalEntry current = mEntries.get(position);
                holder.bind(current);
            }
        }

        @Override
        public int getItemCount() {
            return (mEntries == null) ? 0 : mEntries.size();
        }

        public void setEntries(List<JournalEntry> entries) {
            mEntries = entries;
            notifyDataSetChanged();
        }
    }
}
