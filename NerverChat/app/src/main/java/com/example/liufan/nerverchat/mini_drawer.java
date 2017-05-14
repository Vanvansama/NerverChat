package com.example.liufan.nerverchat;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
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
import com.mikepenz.iconics.IconicsDrawable;
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
import com.mikepenz.materialdrawer.model.ProfileSettingDrawerItem;
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

    //user
    user user;
    //FrameLayout
    private FrameLayout layout;

    //header and result
    private AccountHeader headerResult = null;
    private Drawer result = null;
    private CrossfadeDrawerLayout crossfadeDrawerLayout = null;
    IProfile profile;
    IProfile profile2;
    IProfile profile3;
    int MainImage;

    //chat list
    List<Chat> chats;

    //recycler
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
            if(mChat.getImageID() != 0){
                mImageView.setImageResource(mChat.getImageID());
            }
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
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //设置顶部状态栏颜色
            window.setStatusBarColor(Color.TRANSPARENT);
            //设置底部导航栏颜色(有的手机没有)
            window.setNavigationBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_mini_toolbar);

        //get intent
        Intent intent = getIntent();
        user = (user) intent.getSerializableExtra("user");
        if(user.getUsername().equals("root")){
            MainImage=R.drawable.user;
        }else if(user.getUsername().equals("yuki")){
            MainImage=R.drawable.yuki;
        }

        //recycler view
        mChatRecyclerView = (RecyclerView) findViewById(R.id.chat_recycler_view);
        mChatRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mChatRecyclerView.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL_LIST));
        updateUI();

        //recyclerView listener
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
                        ChatHolder viewHolder = (ChatHolder) mChatRecyclerView.getChildViewHolder(view);
                        layout = (FrameLayout) findViewById(R.id.frame_container) ;
                        Snackbar.make(layout,viewHolder.mChat.getName()+" touch!",Snackbar.LENGTH_LONG)
                                .setAction("Undo", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        // Perform anything for the action selected
                                    }
                                }).show();
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
                                Intent intent = new Intent(mini_drawer.this,ChatLayout.class);
                                Bundle data = new Bundle();
                                data.putSerializable("user",user);
                                data.putSerializable("chat",chats.get(position));
                                intent.putExtras(data);
                                startActivity(intent);
                            }
                        })
                .create();
        mChatRecyclerView.setOnTouchListener(listener);


        //toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //create the AccountHeader
        headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.header)
                .addProfiles(
                        profile = new ProfileDrawerItem().withName(user.getUsername().toString()).withEmail("*********@qq.com").withIcon(MainImage)
                        //new ProfileSettingDrawerItem().withName("Add Friend").withDescription("Add new Friend").withIcon(GoogleMaterial.Icon.gmd_plus).withIdentifier(100001),
                        //new ProfileSettingDrawerItem().withName("Manage Account").withIcon(GoogleMaterial.Icon.gmd_settings).withIdentifier(100002)
                )
                .withSelectionListEnabled(false)
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean current) {
                        if(profile instanceof IDrawerItem){
                            Toast.makeText(mini_drawer.this, "1234", Toast.LENGTH_SHORT).show();
                        }
                        /*
                        //sample usage of the onProfileChanged listener
                        //if the clicked item has the identifier 1 add a new profile ;)
                        if (profile instanceof IDrawerItem && profile.getIdentifier() == 100001) {
                            int count = 100 + headerResult.getProfiles().size() + 1;
                            IProfile newProfile = new ProfileDrawerItem().withNameShown(true).withName("new" + count).withEmail("new" + count + "@gmail.com").withIcon(R.drawable.profile5).withIdentifier(count);
                            if (headerResult.getProfiles() != null) {
                                //we know that there are 2 setting elements. set the new profile above them ;)
                                headerResult.addProfile(newProfile, headerResult.getProfiles().size() - 2);
                            } else {
                                headerResult.addProfiles(newProfile);
                            }
                        }
                        //false if you have not consumed the event and it should close the drawer
                        */
                        return false;
                    }
                })
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
                        new PrimaryDrawerItem().withName("Message")
                                .withIcon(FontAwesome.Icon.faw_home)
                                .withBadge("3")
                                .withBadgeStyle(new BadgeStyle(Color.RED, Color.RED))
                                .withIdentifier(1),
                        new PrimaryDrawerItem().withName("Item 1")
                                .withIcon(GoogleMaterial.Icon.gmd_sun)
                                .withIdentifier(2),
                        new PrimaryDrawerItem()
                                .withName("Item 3")
                                .withIcon(FontAwesome.Icon.faw_gamepad)
                                .withIdentifier(3),
                        new PrimaryDrawerItem()
                                .withName("Item 4")
                                .withIcon(FontAwesome.Icon.faw_eye)
                                .withIdentifier(4),
                        new PrimaryDrawerItem()
                                .withDescription("A more complex sample")
                                .withName("Item 5")
                                .withIcon(GoogleMaterial.Icon.gmd_adb)
                                .withIdentifier(5),
                        new SectionDrawerItem().withName("Settings option"),
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.mini_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){




            default:
                return super.onOptionsItemSelected(item);
        }
    }



    private void updateUI(){
        ChatLab chatLab = ChatLab.get(user);
        chats = chatLab.getChats();

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
