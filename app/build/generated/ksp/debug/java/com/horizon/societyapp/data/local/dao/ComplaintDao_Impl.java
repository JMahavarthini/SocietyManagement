package com.horizon.societyapp.data.local.dao;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.horizon.societyapp.data.local.Converters;
import com.horizon.societyapp.data.local.entity.ComplaintEntity;
import com.horizon.societyapp.data.model.ComplaintStatus;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class ComplaintDao_Impl implements ComplaintDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<ComplaintEntity> __insertionAdapterOfComplaintEntity;

  private final Converters __converters = new Converters();

  private final EntityDeletionOrUpdateAdapter<ComplaintEntity> __updateAdapterOfComplaintEntity;

  public ComplaintDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfComplaintEntity = new EntityInsertionAdapter<ComplaintEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `complaints` (`id`,`title`,`description`,`category`,`raisedByName`,`raisedByFlat`,`status`,`timestamp`) VALUES (nullif(?, 0),?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ComplaintEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getTitle());
        statement.bindString(3, entity.getDescription());
        statement.bindString(4, entity.getCategory());
        statement.bindString(5, entity.getRaisedByName());
        statement.bindString(6, entity.getRaisedByFlat());
        final String _tmp = __converters.fromComplaintStatus(entity.getStatus());
        statement.bindString(7, _tmp);
        statement.bindLong(8, entity.getTimestamp());
      }
    };
    this.__updateAdapterOfComplaintEntity = new EntityDeletionOrUpdateAdapter<ComplaintEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `complaints` SET `id` = ?,`title` = ?,`description` = ?,`category` = ?,`raisedByName` = ?,`raisedByFlat` = ?,`status` = ?,`timestamp` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ComplaintEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getTitle());
        statement.bindString(3, entity.getDescription());
        statement.bindString(4, entity.getCategory());
        statement.bindString(5, entity.getRaisedByName());
        statement.bindString(6, entity.getRaisedByFlat());
        final String _tmp = __converters.fromComplaintStatus(entity.getStatus());
        statement.bindString(7, _tmp);
        statement.bindLong(8, entity.getTimestamp());
        statement.bindLong(9, entity.getId());
      }
    };
  }

  @Override
  public Object insert(final ComplaintEntity complaint,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfComplaintEntity.insert(complaint);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object update(final ComplaintEntity complaint,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfComplaintEntity.handle(complaint);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<ComplaintEntity>> getAllComplaints() {
    final String _sql = "SELECT * FROM complaints ORDER BY timestamp DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"complaints"}, new Callable<List<ComplaintEntity>>() {
      @Override
      @NonNull
      public List<ComplaintEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfRaisedByName = CursorUtil.getColumnIndexOrThrow(_cursor, "raisedByName");
          final int _cursorIndexOfRaisedByFlat = CursorUtil.getColumnIndexOrThrow(_cursor, "raisedByFlat");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final List<ComplaintEntity> _result = new ArrayList<ComplaintEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ComplaintEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpDescription;
            _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            final String _tmpCategory;
            _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            final String _tmpRaisedByName;
            _tmpRaisedByName = _cursor.getString(_cursorIndexOfRaisedByName);
            final String _tmpRaisedByFlat;
            _tmpRaisedByFlat = _cursor.getString(_cursorIndexOfRaisedByFlat);
            final ComplaintStatus _tmpStatus;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfStatus);
            _tmpStatus = __converters.toComplaintStatus(_tmp);
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            _item = new ComplaintEntity(_tmpId,_tmpTitle,_tmpDescription,_tmpCategory,_tmpRaisedByName,_tmpRaisedByFlat,_tmpStatus,_tmpTimestamp);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<ComplaintEntity>> getComplaintsForResident(final String flatNumber) {
    final String _sql = "SELECT * FROM complaints WHERE raisedByFlat = ? ORDER BY timestamp DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, flatNumber);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"complaints"}, new Callable<List<ComplaintEntity>>() {
      @Override
      @NonNull
      public List<ComplaintEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfRaisedByName = CursorUtil.getColumnIndexOrThrow(_cursor, "raisedByName");
          final int _cursorIndexOfRaisedByFlat = CursorUtil.getColumnIndexOrThrow(_cursor, "raisedByFlat");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final List<ComplaintEntity> _result = new ArrayList<ComplaintEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ComplaintEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpDescription;
            _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            final String _tmpCategory;
            _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            final String _tmpRaisedByName;
            _tmpRaisedByName = _cursor.getString(_cursorIndexOfRaisedByName);
            final String _tmpRaisedByFlat;
            _tmpRaisedByFlat = _cursor.getString(_cursorIndexOfRaisedByFlat);
            final ComplaintStatus _tmpStatus;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfStatus);
            _tmpStatus = __converters.toComplaintStatus(_tmp);
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            _item = new ComplaintEntity(_tmpId,_tmpTitle,_tmpDescription,_tmpCategory,_tmpRaisedByName,_tmpRaisedByFlat,_tmpStatus,_tmpTimestamp);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
