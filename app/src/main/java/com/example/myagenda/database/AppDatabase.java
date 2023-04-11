package com.example.myagenda.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.myagenda.database.Dao.BookDao;
import com.example.myagenda.database.Dao.UserDao;

@Database(version = 5,
        entities = {User.class, Book.class}
        )
public abstract class AppDatabase extends RoomDatabase {

    public abstract UserDao userDao();
    public abstract BookDao bookDao();

    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL(
                    "CREATE TABLE IF NOT EXISTS `Book` (`" +
                            "id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                            "`title` TEXT NOT NULL, " +
                            "`subtitle` TEXT NOT NULL, " +
                            "`authors` TEXT NOT NULL, " +
                            "`publisher` TEXT NOT NULL, " +
                            "`publishedDate` TEXT NOT NULL, " +
                            "`description` TEXT NOT NULL, " +
                            "`thumbnail` TEXT NOT NULL, " +
                            "`email` TEXT NOT NULL, " +
                            "`isRead` INTEGER NOT NULL)"
            );
        }
    };

    static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL(
                    "CREATE TABLE IF NOT EXISTS `Book_backup` (" +
                            "`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                            "`title` TEXT NOT NULL, " +
                            "`subtitle` TEXT NOT NULL, " +
                            "`authors` TEXT NOT NULL, " +
                            "`description` TEXT NOT NULL, " +
                            "`thumbnail` TEXT NOT NULL, " +
                            "`email` TEXT NOT NULL, " +
                            "`isRead` INTEGER NOT NULL)"
            );
            database.execSQL("INSERT INTO `Book_backup`" +
                    "SELECT `id`, `title`, `subtitle`, `authors`, `description`, `thumbnail`, `email`, `isRead` FROM `Book`");

            database.execSQL("DROP TABLE `Book`");
            database.execSQL("ALTER TABLE `Book_Backup` RENAME to `Book`");
        }
    };

    static final Migration MIGRATION_3_4 = new Migration(3, 4) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL(
                    "ALTER TABLE `Book`" +
                            "ADD COLUMN `finishedDate` TEXT NOT NULL"
            );
        }
    };

    static final Migration MIGRATION_4_5 = new Migration(4, 5) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL(
                    "ALTER TABLE `Book`" +
                            "ADD COLUMN `isHttpsImage` BOOLEAN NOT NULL"
            );
        }
    };
}
