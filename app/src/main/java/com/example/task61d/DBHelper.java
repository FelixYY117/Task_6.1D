package com.example.task61d;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import okhttp3.OkHttpClient;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;    


public class DBHelper extends SQLiteOpenHelper {
  
    private static final String DATABASE_NAME = "UserDB";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "User";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PASSWORD = "password";

  
    private static final String TABLE_USER_INTERESTS = "user_interests";
    private static final String COLUMN_USER_INTEREST_USERNAME = "username";
    private static final String COLUMN_USER_INTEREST_INTEREST_NAME = "interest_name"; // 省略常量定义...

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 获取数据库对象
        // 执行 SQL 语句创建 User 表
        db.execSQL(CREATE_TABLE);
        // 执行 SQL 语句创建 user_interests 表
        db.execSQL(createUserInterestsTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 获取数据库对象和版本信息
        // 删除已存在的 User 表
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        // 调用 onCreate 方法重新创建表
        onCreate(db);
    }

    public boolean addUser(String username, String email, String password) {
        // 获取可写的数据库对象
        SQLiteDatabase db = this.getWritableDatabase();
        // 创建一个 ContentValues 对象,存储要插入的用户信息
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_PASSWORD, password);
        values.put(COLUMN_EMAIL, email);
        // 将 ContentValues 对象插入到 User 表中
        long userId = db.insert(TABLE_NAME, null, values);
        // 关闭数据库连接
        db.close();
        // 根据插入结果返回 boolean 值
        return userId != -1;
    }

    public boolean checkUser(String username, String password) {
        // 获取可写的数据库对象
        SQLiteDatabase db = this.getWritableDatabase();
        // 定义查询列
        String[] columns = {COLUMN_ID};
        // 构造查询条件和参数
        String selection = COLUMN_USERNAME + " = ?" + " AND " + COLUMN_PASSWORD + " = ?";
        String[] selectionArgs = {username, password};
        // 执行查询
        Cursor cursor = db.query(TABLE_NAME, columns, selection, selectionArgs, null, null, null);
        // 获取查询结果的行数
        int count = cursor.getCount();
        // 关闭游标和数据库连接
        cursor.close();
        db.close();
        // 根据行数返回 boolean 值
        return count > 0;
    }

    public void insertUserInterest(String username, String interestName) {
        // 创建 DBHelper 实例
        DBHelper dbHelper = this;
        // 检查给定的兴趣是否已存在
        if (!dbHelper.isInterestExist(username, interestName)) {
            // 获取可写的数据库对象
            SQLiteDatabase db = this.getWritableDatabase();
            // 创建 ContentValues 对象,存储要插入的用户和兴趣
            ContentValues values = new ContentValues();
            values.put(COLUMN_USER_INTEREST_USERNAME, username);
            values.put(COLUMN_USER_INTEREST_INTEREST_NAME, interestName);
            // 将 ContentValues 对象插入到 user_interests 表中
            db.insert(TABLE_USER_INTERESTS, null, values);
            // 关闭数据库连接
            db.close();
        }
    }

    public List<String> getUserInterests(String username) {
        // 创建一个空的 List 对象用于存储兴趣
        List<String> interests = new ArrayList<>();
        // 获取可读的数据库对象
        SQLiteDatabase db = this.getReadableDatabase();
        // 执行查询,获取给定用户的所有兴趣
        Cursor cursor = db.query(TABLE_USER_INTERESTS, new String[]{COLUMN_USER_INTEREST_INTEREST_NAME},
                COLUMN_USER_INTEREST_USERNAME + "=?", new String[]{username}, null, null, null);
        // 遍历查询结果,将每个兴趣添加到 List 中
        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String interestName = cursor.getString(cursor.getColumnIndex(COLUMN_USER_INTEREST_INTEREST_NAME));
                interests.add(interestName);
            } while (cursor.moveToNext());
        }
        // 关闭游标和数据库连接
        cursor.close();
        db.close();
        // 返回包含所有兴趣的 List
        return interests;
    }

    public boolean isInterestExist(String username, String interestName) {
        // 构造 SQL 查询语句
        String query = "SELECT 1 FROM user_interests WHERE username = ? AND interest_name = ? LIMIT 1";
        // 获取可读的数据库对象
        SQLiteDatabase db = getReadableDatabase();
        // 执行原始查询
        Cursor cursor = db.rawQuery(query, new String[]{username, interestName});
        // 检查查询结果是否有数据
        boolean exist = cursor.moveToFirst();
        // 关闭游标
        cursor.close();
        // 根据查询结果返回 boolean 值
        return exist;
    }
}
