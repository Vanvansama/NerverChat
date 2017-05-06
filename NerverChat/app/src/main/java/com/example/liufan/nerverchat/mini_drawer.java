package com.example.liufan.nerverchat;

import android.graphics.Color;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.liufan.nerverchat.my.DividerItemDecoration;
import com.example.liufan.nerverchat.my.SwipeDismissRecyclerViewTouchListener;
import com.mikepenz.crossfadedrawerlayout.view.CrossfadeDrawerLayout;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.MiniDrawer;
import com.mikepenz.materialdrawer.holder.BadgeStyle;
import com.mikepenz.materialdrawer.interfaces.ICrossfader;
import com.mikepenz.materialdrawer.interfaces.OnCheckedChangeListener;
import com.mikepenz.materialdrawer.model.ExpandableBadgeDrawerItem;
import com.mikepenz.materialdrawer.model.ExpandableDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondarySwitchDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryToggleDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mikepenz.materialdrawer.model.interfaces.Nameable;
import com.mikepenz.materialdrawer.util.DrawerUIUtils;
import com.mikepenz.materialize.util.UIUtils;
import com.mikepenz.octicons_typeface_library.Octicons;

import java.util.List;


public class mini_drawer extends AppCompatActivity {
    //FrameLayout
    private FrameLayout layout;

    //header and result
    private AccountHeader headerResult = null;
    private Drawer result = null;
    private CrossfadeDrawerLayout crossfadeDrawerLayout = null;

    IProfile profile = new ProfileDrawerItem().withName("Mike Penz").withEmail("mikepenz@gmail.com").withIcon("https://avatars3.githubusercontent.com/u/1476232?v=3&s=460");
    IProfile profile2 = new ProfileDrawerItem().withName("Bernat Borras").withEmail("alorma@github.com").withIcon(Uri.parse("https://avatars3.githubusercontent.com/u/887462?v=3&s=460"));

    private RecyclerView mChatRecyclerView;
    private ChatAdapter mAdapter;
    //recycler adapter
    private class ChatHolder extends RecyclerView.ViewHolder{
        private Chat mChat;
        private ImageView mImageView;
        private TextView mTitleTextView;
        private TextView mSummaryView;
        public ChatHolder(View itemView){
            super(itemView);

            mImageView = (ImageView) itemView.findViewById(R.id.list_item_imageView);
            mSummaryView = (TextView) itemView.findViewById(R.id.list_item_summaryTextView);
            mTitleTextView = (TextView) itemView.findViewById(R.id.list_item_nameTextView);
        }
        public void bindChat(Chat chat){
            mChat = chat;
            mTitleTextView.setText(mChat.getName().toString());
            mSummaryView.setText("-"+mChat.getName().toString()+"-");
        }
    }
    private class ChatAdapter extends RecyclerView.Adapter<ChatHolder>{
        private List<Chat> mChats;


        public ChatAdapter (List<Chat> chats){
            mChats = chats;
        }
        @Override
        public ChatHolder onCreateViewHolder(ViewGroup parent, int viewType){
            LayoutInflater layoutInflater = LayoutInflater.from(mini_drawer.this);
            View view = layoutInflater.inflate(R.layout.list_item_chat, parent, false);
            return new ChatHolder(view);
        }
        @Override
        public void onBindViewHolder(ChatHolder holder, int position){
            Chat chat = mChats.get(position);
            holder.bindChat(chat);
        }
        @Override
        public int getItemCount(){
            return mChats.size();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mini_toolbar);

        //recycler view
        mChatRecyclerView = (RecyclerView) findViewById(R.id.chat_recycler_view);
        mChatRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mChatRecyclerView.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL_LIST));
        updateUI();

        //recycler listen
        SwipeDismissRecyclerViewTouchListener listener = new SwipeDismissRecyclerViewTouchListener.Builder(
                mChatRecyclerView,
                new SwipeDismissRecyclerViewTouchListener.DismissCallbacks(){
                    @Override
                    public boolean canDismiss(int positon){
                        return true;
                    }
                    @Override
                    public void onDismiss(View view){
                        //Do what you want when dismiss

                    }
                })
                .setIsVertical(false)
                .setItemTouchCallback(
                        new SwipeDismissRecyclerViewTouchListener.OnItemTouchCallBack() {
                            @Override
                            public void onTouch(int position) {
                                //Do what you want when item be touched
                            }
                        })
                .setItemClickCallback(
                        new SwipeDismissRecyclerViewTouchListener.OnItemClickCallBack() {
                            @Override
                            public void onClick(int position) {
                                //Do what you want when item be click
                                View view = mChatRecyclerView.getChildAt(position);
                                ChatHolder viewHolder = (ChatHolder) mChatRecyclerView.getChildViewHolder(view);
                                layout = (FrameLayout) findViewById(R.id.frame_container) ;
                                Snackbar.make(layout,viewHolder.mChat.getName()+" click!",Snackbar.LENGTH_LONG)
                                        .setAction("Undo", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                // Perform anything for the action selected
                                            }
                                        }).show();
                            }
                        })
                .create();
        mChatRecyclerView.setOnTouchListener(listener);


        //handle toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //create the AccountHeader
        headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.header)
                .addProfiles(
                        profile,profile2
                )
                .withSavedInstance(savedInstanceState)
                .build();

        //create the drawer
        result = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withHasStableIds(true)
                .withDrawerLayout(R.layout.crossfade_drawer)
                .withDrawerWidthDp(72)
                .withGenerateMiniDrawer(true)
                .withAccountHeader(headerResult)//set the AccountHeader we created earlier for the header
                .addDrawerItems(
                        new PrimaryDrawerItem().withName("Item 1")
                                .withIcon(GoogleMaterial.Icon.gmd_sun).withIdentifier(1),
                        new PrimaryDrawerItem().withName("Item 2")
                                .withIcon(FontAwesome.Icon.faw_home).withBadge("22")
                                .withBadgeStyle(new BadgeStyle(Color.RED, Color.RED)).withIdentifier(2).withSelectable(false),
                        new PrimaryDrawerItem()
                                .withName("Item 3")
                                .withIcon(FontAwesome.Icon.faw_gamepad).withIdentifier(3),
                        new PrimaryDrawerItem()
                                .withName("Item 4")
                                .withIcon(FontAwesome.Icon.faw_eye)
                                .withIdentifier(4),
                        new PrimaryDrawerItem()
                                .withDescription("A more complex sample")
                                .withName("Item 5")
                                .withIcon(GoogleMaterial.Icon.gmd_adb)
                                .withIdentifier(5),
                        new SectionDrawerItem().withName("Section Header"),
                        new ExpandableBadgeDrawerItem()
                                .withName("Collapsable Badge")
                                .withIcon(GoogleMaterial.Icon.gmd_collection_case_play)
                                .withIdentifier(6).withSelectable(false)
                                .withBadgeStyle(new BadgeStyle()
                                        .withTextColor(Color.WHITE)
                                        .withColorRes(R.color.md_red_700))
                                .withBadge("100")
                                .withSubItems(
                                        new SecondaryDrawerItem()
                                                .withName("CollapsableItem")
                                                .withLevel(2)
                                                .withIcon(GoogleMaterial.Icon.gmd_8tracks)
                                                .withIdentifier(2000),
                                        new SecondaryDrawerItem()
                                                .withName("CollapsableItem 2")
                                                .withLevel(2)
                                                .withIcon(GoogleMaterial.Icon.gmd_8tracks)
                                                .withIdentifier(2001)
                                ),
                        new ExpandableDrawerItem()
                                .withName("Collapsable")
                                .withIcon(GoogleMaterial.Icon.gmd_collection_case_play)
                                .withIdentifier(7).withSelectable(false)
                                .withSubItems(
                                        new SecondarySwitchDrawerItem()
                                                .withName("Secondary switch")
                                                .withIcon(Octicons.Icon.oct_tools)
                                                .withChecked(true)
                                                .withOnCheckedChangeListener(onCheckedChangeListener),
                                        new SecondarySwitchDrawerItem()
                                                .withName("Secondary Switch2")
                                                .withIcon(Octicons.Icon.oct_tools)
                                                .withChecked(true)
                                                .withOnCheckedChangeListener(onCheckedChangeListener)
                                                .withSelectable(false),
                                        new SecondaryToggleDrawerItem()
                                                .withName("Secondary toggle")
                                                .withIcon(Octicons.Icon.oct_tools)
                                                .withChecked(true)
                                                .withOnCheckedChangeListener(onCheckedChangeListener)
                                ),
                        new SecondaryDrawerItem().withName("item 1").withIcon(FontAwesome.Icon.faw_github).withIdentifier(8).withSelectable(false),
                        new SecondaryDrawerItem().withName("item 2").withIcon(GoogleMaterial.Icon.gmd_format_color_fill).withIdentifier(9).withTag("Bullhorn")
                ) // add the items we want to use with our Drawer
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        if (drawerItem instanceof Nameable) {
                            Toast.makeText(mini_drawer.this, ((Nameable) drawerItem).getName().getText(mini_drawer.this), Toast.LENGTH_SHORT).show();
                        }
                        //we do not consume the event and want the Drawer to continue with the event chain
                        return false;
                    }
                })//listening the click
                .withSavedInstance(savedInstanceState)
                .withShowDrawerOnFirstLaunch(true)
                .build();


        //get the CrossfadeDrawerLayout which will be used as alternative DrawerLayout for the Drawer
        crossfadeDrawerLayout = (CrossfadeDrawerLayout) result.getDrawerLayout();

        //define maxDrawerWidth
        crossfadeDrawerLayout.setMaxWidthPx(DrawerUIUtils.getOptimalDrawerWidth(this));
        //add second view (which is the miniDrawer)
        final MiniDrawer miniResult = result.getMiniDrawer();
        //build the view for the MiniDrawer
        View view = miniResult.build(this);
        //set the background of the MiniDrawer as this would be transparent
        view.setBackgroundColor(UIUtils.getThemeColorFromAttrOrRes(this, com.mikepenz.materialdrawer.R.attr.material_drawer_background, com.mikepenz.materialdrawer.R.color.material_drawer_background));
        //we do not have the MiniDrawer view during CrossfadeDrawerLayout creation so we will add it here
        crossfadeDrawerLayout.getSmallView().addView(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        //define the crossfader to be used with the miniDrawer. This is required to be able to automatically toggle open / close
        miniResult.withCrossFader(new ICrossfader() {
            @Override
            public void crossfade() {
                boolean isFaded = isCrossfaded();
                crossfadeDrawerLayout.crossfade(400);

                //only close the drawer if we were already faded and want to close it now
                if (isFaded) {
                    result.getDrawerLayout().closeDrawer(GravityCompat.START);
                }
            }

            @Override
            public boolean isCrossfaded() {
                return crossfadeDrawerLayout.isCrossfaded();
            }
        });
    }



    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //add the values which need to be saved from the drawer to the bundle
        outState = result.saveInstanceState(outState);
        //add the values which need to be saved from the accountHeader to the bundle
        outState = headerResult.saveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        //handle the back press :D close the drawer first and if the drawer is closed close the activity
        if (result != null && result.isDrawerOpen()) {
            result.closeDrawer();
        } else {
            super.onBackPressed();
        }
    }



    private void updateUI(){
        ChatLab chatLab = ChatLab.get(mini_drawer.this);
        List<Chat> chats = chatLab.getChats();

        mAdapter = new ChatAdapter(chats);
        mChatRecyclerView.setAdapter(mAdapter);
    }

    private OnCheckedChangeListener onCheckedChangeListener = new OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(IDrawerItem drawerItem, CompoundButton buttonView, boolean isChecked) {
            if (drawerItem instanceof Nameable) {
                Log.i("material-drawer", "DrawerItem: " + ((Nameable) drawerItem).getName() + " - toggleChecked: " + isChecked);
            } else {
                Log.i("material-drawer", "toggleChecked: " + isChecked);
            }
        }
    };

}
