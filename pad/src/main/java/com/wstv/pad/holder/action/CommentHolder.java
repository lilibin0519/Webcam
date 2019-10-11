package com.wstv.pad.holder.action;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;

import com.libin.mylibrary.util.GlideLoadUtils;
import com.wstv.pad.R;
import com.wstv.pad.http.model.action.Comment;

import java.util.List;

import em.sang.com.allrecycleview.holder.CustomHolder;

/**
 * <p>Description: </p>
 * CommentHolder
 *
 * @author lilibin
 * @createDate 2019/5/24 11:00
 */

public class CommentHolder extends CustomHolder<Comment> {

    public CommentHolder(Context context, List<Comment> lists, int itemID) {
        super(context, lists, itemID);
    }

    @Override
    public void initView(int position, List<Comment> datas, Context context) {
//        Glide.with(context).load(datas.get(position).user.headPortrait).dontAnimate().placeholder(R.drawable.default_avatar).error(R.drawable.default_avatar)
//                .into((ImageView) holderHelper.getView(R.id.item_action_comment_avatar));
        GlideLoadUtils.getInstance().glideLoad(context, datas.get(position).user.headPortrait,
                (ImageView) holderHelper.getView(R.id.item_action_comment_avatar), R.drawable.default_avatar);
        holderHelper.setText(R.id.item_action_comment_name, datas.get(position).user.nickname);
        holderHelper.setText(R.id.item_action_comment_content, (TextUtils.isEmpty(datas.get(position).callUser) ? "" : ("回复" + datas.get(position).callUser + ":")) + datas.get(position).comment);
        holderHelper.setText(R.id.item_action_comment_time, datas.get(position).createTime);
    }
}
