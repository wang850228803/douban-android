package com.douban.book

import com.douban.base.{DoubanActivity, DoubanFragment}
import android.view.{View, ViewGroup, LayoutInflater}
import android.os.Bundle
import scala.concurrent._
import com.douban.models.{Book, User}
import scala.util.Success
import org.scaloid.common._
import android.widget.LinearLayout
import ExecutionContext.Implicits.global


/**
 * Copyright by <a href="http://crazyadam.net"><em><i>Joseph J.C. Tang</i></em></a> <br/>
 * Email: <a href="mailto:jinntrance@gmail.com">jinntrance@gmail.com</a>
 * @author joseph
 * @since 10/7/13 3:24 PM
 * @version 1.0
 */
class MenuActivity {
}

class MenuFragment extends DoubanFragment[DoubanActivity]{
  override def onCreateView(inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle): View = inflater.inflate(R.layout.settings,container,false)

  override def onActivityCreated(b: Bundle) {
    super.onActivityCreated(b)
    future {
       getThisActivity.waitToLoad(getThisActivity.finishedLoading())
      (User.byId(getThisActivity.currentUserId),Book.collectionsOfUser(getThisActivity.currentUserId),Book.annotationsOfUser(getThisActivity.currentUserId.toString))
    } onComplete{
      case Success((u,c,n))=>runOnUiThread{ //TODO
        setViewValue(R.id.username,u.name)
        setViewValue(R.id.collection_num,c.total.toString)
        setViewValue(R.id.notes_num,n.total.toString)
        loadImageWithTitle(u.avatar,R.id.user_avatar,u.name)
      }
      case _=>
    }
   getView.find[LinearLayout](R.id.menu_search) onClick(v=>startActivity[SearchActivity])
   getView.find[LinearLayout](R.id.menu_mynote) onClick(v=>startActivity[MyNoteActivity])
   getView.find[LinearLayout](R.id.menu_favbooks) onClick(v=>startActivity[FavoriteBooksActivity])
   getView.find[LinearLayout](R.id.menu_settings) onClick(v=>startActivity[SettingsActivity])
  }
}