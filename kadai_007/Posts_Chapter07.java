package kadai_007;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Posts_Chapter07 {
	public static void main(String[] args) {
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		// ポストリスト
		String[][] postsList = {
				{ "1003", "2023-02-08", "昨日の夜は徹夜でした・・", "13" },
				{ "1002", "2023-02-08", "お疲れ様です！", "12" },
				{ "1003", "2023-02-09", "今日も頑張ります！", "18" },
				{ "1001", "2023-02-09", "無理は禁物ですよ！", "17" },
				{ "1002", "2023-02-10", "明日から連休ですね！", "20" },
		};

		try {
			// データベースに接続
			con = DriverManager.getConnection("jdbc:mysql://localhost/challenge_java", "root", "root");
			System.out.println("データベース接続成功");

			// SQLクエリを準備
			String sql = "INSERT INTO posts (user_id, posted_at, post_content, likes) VALUES (?, ?, ?, ?)";
			preparedStatement = con.prepareStatement(sql);

			// レコード追加を実行します
			System.out.println("レコード追加を実行します");
			for (String[] post : postsList) {
				preparedStatement.setString(1, post[0]); // ユーザーID
				preparedStatement.setDate(2, Date.valueOf(post[1])); // 投稿日時
				preparedStatement.setString(3, post[2]); // 投稿内容
				preparedStatement.setInt(4, Integer.parseInt(post[3])); // いいね数

				// SQLクエリを実行（DBMSに送信）
				preparedStatement.executeUpdate();
			}
			System.out.println(postsList.length + "件のレコードが追加されました");

			// SQLクエリを準備
			sql = "SELECT * FROM posts WHERE user_id = 1002";
			preparedStatement = con.prepareStatement(sql);
			
			System.out.println("ユーザーIDが1002のレコードを検索しました");

			// SQLクエリを実行（DBMSに送信）
			resultSet = preparedStatement.executeQuery();

			// SQLクエリの実行結果を抽出
			while (resultSet.next()) {
				Date postedAt = resultSet.getDate("posted_at");
				String postContent = resultSet.getString("post_content");
				int likes = resultSet.getInt("likes");
				System.out.println(
						resultSet.getRow() + "件目：投稿日時=" + postedAt + "／投稿内容=" + postContent + "／いいね数=" + likes);
			}

		} catch (SQLException e) {
			System.out.println("エラー発生：" + e.getMessage());
		} finally {
			// 使用したオブジェクトを解放
			if (resultSet != null) {
				try {
					resultSet.close();
				} catch (SQLException ignore) {
				}
			}
			if (preparedStatement != null) {
				try {
					preparedStatement.close();
				} catch (SQLException ignore) {
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (SQLException ignore) {
				}
			}
		}
	}
}
