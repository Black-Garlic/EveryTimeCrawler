
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class Main {

	public static final String WEB_DRIVER_ID = "webdriver.chrome.driver"; // 드라이버 ID
	public static final String WEB_DRIVER_PATH = "C:\\Drivers\\chromedriver.exe"; // 드라이버 경로
	public static ArrayList<Lecture> lectureList = new ArrayList<Lecture>();

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		setLectureList();
		
		// 드라이버 설정
		try {
			System.setProperty(WEB_DRIVER_ID, WEB_DRIVER_PATH);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 크롬 설정을 담은 객체 생성
		ChromeOptions options = new ChromeOptions();
		// 브라우저가 눈에 보이지 않고 내부적으로 돈다.
		// 설정하지 않을 시 실제 크롬 창이 생성되고, 어떤 순서로 진행되는지 확인할 수 있다.
		// options.addArguments("headless");

		// 위에서 설정한 옵션은 담은 드라이버 객체 생성
		// 옵션을 설정하지 않았을 때에는 생략 가능하다.
		// WebDriver객체가 곧 하나의 브라우저 창이라 생각한다.
		WebDriver driver = new ChromeDriver(options);

		driver.get("https://everytime.kr/login");

		try {Thread.sleep(1000);} catch (InterruptedException e) {}

		WebElement loginId = driver.findElement(By.name("userid"));
		loginId.sendKeys("samuel1226");
		WebElement loginPassword = driver.findElement(By.name("password"));
		loginPassword.sendKeys("hn1020011");
		WebElement loginButton = driver.findElement(By.xpath("//*[@id=\"container\"]/form/p[3]/input"));
		loginButton.click();

		// 브라우저 이동시 생기는 로드시간을 기다린다.
		// HTTP응답속도보다 자바의 컴파일 속도가 더 빠르기 때문에 임의적으로 1초를 대기한다.
		try {Thread.sleep(1000);} catch (InterruptedException e) {}

		for (int lectureCount = 0; lectureCount < 3; lectureCount++) {
			for (int sectionCount = 0; sectionCount < lectureList.get(lectureCount).getSectionList().size(); sectionCount++) {
				String url = "https://everytime.kr/lecture/view/" + lectureList.get(lectureCount).getSectionList().get(sectionCount).getSectionCode();
				driver.get(url);
				
				try {Thread.sleep(1000);} catch (InterruptedException e) {}
				
				WebElement lectureProfessor = driver.findElement(By.xpath("//*[@id=\"container\"]/div[2]/p[1]/span"));

				WebElement lectureScore = driver.findElement(By.xpath("//*[@id=\"container\"]/div[4]/div[1]/div[1]/span/span[1]"));

				WebElement lectureHomework = driver.findElement(By.xpath("//*[@id=\"container\"]/div[4]/div[1]/div[2]/p[1]/span"));

				WebElement lectureTeam = driver.findElement(By.xpath("//*[@id=\"container\"]/div[4]/div[1]/div[2]/p[2]/span"));

				WebElement lectureExam = driver.findElement(By.xpath("//*[@id=\"container\"]/div[4]/div[1]/div[2]/p[5]/span"));
				
				lectureList.get(lectureCount).getSectionList().get(sectionCount).setInfo(
						lectureProfessor.getText(),
						lectureScore.getText(),
						lectureHomework.getText(),
						lectureTeam.getText(),
						lectureExam.getText()
						);

				List<WebElement> article = driver.findElement(By.className("articles")).findElements(By.tagName("article"));
				System.out.println(article.size());

				for (int reviewCount = 0; reviewCount < article.size(); reviewCount++) {
					System.out.println(reviewCount);
					int star = article.get(reviewCount).findElement(By.className("rate")).findElement(By.className("star")).findElement(By.className("on")).getSize().width / 12;
					String review = article.get(reviewCount).findElement(By.className("text")).getText();
					lectureList.get(lectureCount).getSectionList().get(sectionCount).addReview(star, review);
				}
			}
		}
		// 1초 대기
		try {Thread.sleep(1000);} catch (InterruptedException e) {}
		
		for (int lectureCount = 0; lectureCount < 3; lectureCount++) {
			String lectureName = lectureList.get(lectureCount).getLectureName();
			
			for (int sectionCount = 0; sectionCount < lectureList.get(lectureCount).getSectionList().size(); sectionCount++) {
				String lectureProfessor = lectureList.get(lectureCount).getSectionList().get(sectionCount).getProfessor();
				
				for (int reviewCount = 0; reviewCount < lectureList.get(lectureCount).getSectionList().get(sectionCount).getReviewList().size(); reviewCount++) {
					int star = lectureList.get(lectureCount).getSectionList().get(sectionCount).getReviewList().get(reviewCount).getStar();
					String review = lectureList.get(lectureCount).getSectionList().get(sectionCount).getReviewList().get(reviewCount).getReview();
					
					//System.out.println(lectureName + " - " + lectureProfessor + " - " + star + " / " + review);
					System.out.println("\"" + review + "\",");
				}
			}
			System.out.println("\n\n\n");
		}
		
		
			
		try {
			// 드라이버가 null이 아니라면
			if (driver != null) {
				// 드라이버 연결 종료
				driver.close(); // 드라이버 연결 해제

				// 프로세스 종료
				driver.quit();
			}
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}

	}

	public static void setLectureList() {
		lectureList.add(new Lecture("ICT", "ICT 입문", "GCS10001", "소프트웨어 입문"));
		lectureList.add(new Lecture("ICT", "소프트웨어 활용", "GCS10007", "오피스 활용"));
		lectureList.add(new Lecture("ICT", "소프트웨어 활용", "GCS20005", "데이터 통계 처리"));
		lectureList.add(new Lecture("ICT", "소프트웨어 활용", "GCS20008", "컴퓨터 그래픽 기초"));
		lectureList.add(new Lecture("ICT", "소프트웨어 활용", "GCS20009", "하이브리드 웹 설계"));
		lectureList.add(new Lecture("ICT", "소프트웨어 활용", "GCS20010", "데이터 수집과 응용"));
		lectureList.add(new Lecture("ICT", "프로그래밍 기초", "GCS10003", "앱 프로그래밍"));
		lectureList.add(new Lecture("ICT", "프로그래밍 기초", "GCS10004", "파이썬 프로그래밍"));
		lectureList.add(new Lecture("ICT", "프로그래밍 기초", "GCS10058", "C 프로그래밍"));
		lectureList.add(new Lecture("ICT", "프로그래밍 기초", "GCS10080", "R을 이용한 빅데이터 분석"));
		lectureList.add(new Lecture("기초학문", "사회과학", "GEK20042", "교육학개론"));
		lectureList.add(new Lecture("기초학문", "사회과학", "GEK10104", "글로벌리제이션과 한국의 대주문화"));
		lectureList.add(new Lecture("기초학문", "사회과학", "GEK10040", "사회학개론"));
		lectureList.add(new Lecture("기초학문", "사회과학", "GEK20044", "시민생활과 법"));
		lectureList.add(new Lecture("기초학문", "사회과학", "GEK10100", "통일한국개론"));
		lectureList.add(new Lecture("기초학문", "인문학", "GEK10035", "Korean History"));
		lectureList.add(new Lecture("기초학문", "인문학", "GEK10087", "동양의 역사와 문화"));
		lectureList.add(new Lecture("기초학문", "인문학", "GEK10088", "서양의 역사와 문화"));
		lectureList.add(new Lecture("기초학문", "인문학", "GEK10039", "역사와 인간"));
		lectureList.add(new Lecture("기초학문", "인문학", "GEK10035", "한국사(근현대사)"));
		lectureList.add(new Lecture("기초학문", "인문학", "GEK10030", "철학개론"));
		lectureList.add(new Lecture("기초학문", "인문학", "GEK10033", "한국문학의 이해"));
		lectureList.add(new Lecture("기초학문", "인문학", "GEK10020", "한국의 전통문화"));
		lectureList.add(new Lecture("기초학문", "인문학", "GEK10101", "한국학개론"));
		lectureList.add(new Lecture("기초학문", "인문학", "GEK30030", "현대과학과 기술의 철학"));
		lectureList.add(new Lecture("기초학문", "인문학", "CCE23005", "한국어학개론"));
		lectureList.add(new Lecture("기초학문", "자연과학", "GEK10095", "Calculus 1"));
		lectureList.add(new Lecture("기초학문", "자연과학", "GEK10096", "Calculus 2"));
		lectureList.add(new Lecture("기초학문", "자연과학", "GEK10097", "Calculus 3"));
		lectureList.add(new Lecture("기초학문", "자연과학", "GEK10081", "공학수학"));
		lectureList.add(new Lecture("기초학문", "자연과학", "GEK10053", "미분방정식과 응용"));
		lectureList.add(new Lecture("기초학문", "자연과학", "GEK10082", "선형대수학"));
		lectureList.add(new Lecture("기초학문", "자연과학", "GEK20053", "통계학"));
		lectureList.add(new Lecture("기초학문", "자연과학", "GEK10055", "물리학 1"));
		lectureList.add(new Lecture("기초학문", "자연과학", "GEK10056", "물리학 2"));
		lectureList.add(new Lecture("기초학문", "자연과학", "GEK10090", "물리학 개론"));
		lectureList.add(new Lecture("기초학문", "자연과학", "GEK10038", "물리학 실험 1"));
		lectureList.add(new Lecture("기초학문", "자연과학", "GEK10070", "생활 속의 생명과학"));
		lectureList.add(new Lecture("기초학문", "자연과학", "GEK10057", "일반생물학"));
		lectureList.add(new Lecture("기초학문", "자연과학", "GEK10058", "일반화학"));
		lectureList.add(new Lecture("기초학문", "자연과학", "GEK10094", "일반화학실험"));
		lectureList.add(new Lecture("리더십 및 문제해결", "리더십", "GEK10080", "대학생을 위한 실용금융"));
		lectureList.add(new Lecture("리더십 및 문제해결", "리더십", "GEK30022", "리더십의 이해"));
		lectureList.add(new Lecture("리더십 및 문제해결", "리더십", "GEK10076", "비전과 진로탐색"));
		lectureList.add(new Lecture("리더십 및 문제해결", "리더십", "GEK20049", "생애진로설계"));
		lectureList.add(new Lecture("리더십 및 문제해결", "리더십", "GEK20024", "세계문화의 이해"));
		lectureList.add(new Lecture("리더십 및 문제해결", "리더십", "GEK20028", "인간관계와 자기성장"));
		lectureList.add(new Lecture("리더십 및 문제해결", "리더십", "GEK30007", "직업과 소명"));
		lectureList.add(new Lecture("리더십 및 문제해결", "리더십", "GEK20074", "창의학습"));
		lectureList.add(new Lecture("리더십 및 문제해결", "리더십", "GEK10110", "한국수어"));
		lectureList.add(new Lecture("리더십 및 문제해결", "리더십", "GEE30023", "Developing Leadership for a Global World"));
		lectureList.add(new Lecture("리더십 및 문제해결", "리더십", "GEE30023", "Developing Leadership for a Global World"));
		lectureList.add(new Lecture("세계관", "세계관 1", "GEK10011", "창조와 진화"));
		lectureList.add(new Lecture("세계관", "세계관 1", "GEK20011", "기독교세계관"));
		lectureList.add(new Lecture("세계관", "세계관 1", "GEK20051", "Mission Perspective"));
		lectureList.add(new Lecture("세계관", "세계관 2", "GEK20016", "신앙특론"));
		lectureList.add(new Lecture("세계관", "세계관 2", "GEK20019", "교회사의 이해"));
		lectureList.add(new Lecture("세계관", "세계관 2", "GEK20035", "신앙과 학문의 통합"));
		lectureList.add(new Lecture("세계관", "세계관 2", "GEK20061", "기독교 윤리"));
		lectureList.add(new Lecture("세계관", "세계관 2", "GEK20062", "기독교 변증학"));
		lectureList.add(new Lecture("세계관", "세계관 2", "GEK30014", "기독교와 현대사상"));
		lectureList.add(new Lecture("세계관", "세계관 2", "GEK30015", "현대사회와 선교"));
		lectureList.add(new Lecture("소통 및 융복합", "소통", "GCS10010", "대학글쓰기 기초"));
		lectureList.add(new Lecture("소통 및 융복합", "소통", "GCS10011", "이공계 글쓰기"));
		lectureList.add(new Lecture("소통 및 융복합", "소통", "GCS10012", "인문사회계 글쓰기"));
		lectureList.add(new Lecture("소통 및 융복합", "소통", "GCS10014", "논리와 비판적 사고"));
		lectureList.add(new Lecture("소통 및 융복합", "소통", "GCS10015", "토론과 발표"));
		lectureList.add(new Lecture("소통 및 융복합", "소통", "GCS10037", "중국어 1"));
		lectureList.add(new Lecture("소통 및 융복합", "소통", "GCS10038", "중국어 2"));
		lectureList.add(new Lecture("소통 및 융복합", "소통", "GCS20054", "한국어 5"));
		lectureList.add(new Lecture("소통 및 융복합", "소통", "GCS20055", "한국어 6"));
		lectureList.add(new Lecture("소통 및 융복합", "융복합", "GCS10016", "고전강독"));
		lectureList.add(new Lecture("소통 및 융복합", "융복합", "GCS10065", "AI 이노베이션"));
		lectureList.add(new Lecture("소통 및 융복합", "융복합", "GEK10007", "교양독서"));
		lectureList.add(new Lecture("소통 및 융복합", "융복합", "GEK10032", "과학기술과 인간정신"));
		lectureList.add(new Lecture("소통 및 융복합", "융복합", "GEK10059", "환경과 인간"));
		lectureList.add(new Lecture("소통 및 융복합", "융복합", "GEK10102", "질병과 건강"));
		lectureList.add(new Lecture("소통 및 융복합", "융복합", "GEK30045", "현대사회와 여성"));
		lectureList.add(new Lecture("소통 및 융복합", "융복합", "GCS10066", "중국 문화와 언어"));
		lectureList.add(new Lecture("신앙 2", "기독교 신앙의 기초 1", "GEK20058", "성경의 이해"));
		lectureList.add(new Lecture("신앙 2", "기독교 신앙의 기초 2", "GEK20059", "기독교의 이해"));
		lectureList.add(new Lecture("신앙 2", "기독교 신앙의 기초 2", "GEK20060", "기독교와 포스트 모더니즘"));
		lectureList.add(new Lecture("신앙 2", "기독교 신앙의 기초 2", "GEK20065", "기독교와 비교종교"));
		lectureList.add(new Lecture("영어", "Level 1", "GCS10053", "English Communication"));
		lectureList.add(new Lecture("영어", "Level 2", "GCS20003", "English Reading and Composition"));
		lectureList.add(new Lecture("영어", "Level 3", "GCS30008", "EAP - Engineering"));
		lectureList.add(new Lecture("영어", "Level 3", "GCS30009", "EAP - Life Science"));
		lectureList.add(new Lecture("영어", "Level 3", "GCS30010", "EAP - Management and Economics"));
		lectureList.add(new Lecture("영어", "Level 3", "GCS30011", "EAP - Counseling Psychology and Social Welfare"));
		lectureList.add(new Lecture("영어", "Level 3", "GCS30012", "EAP - Communication Arts and Science"));
		lectureList.add(new Lecture("영어", "Level 3", "GCS30013", "EAP - Contents Convergence Design"));
		lectureList.add(new Lecture("영어", "Level 3", "GCS30014", "EAP - International Studies and Law"));
		lectureList.add(new Lecture("영어", "Level 3", "GCS30015", "EAP - Information Technology"));
		lectureList.add(new Lecture("영어", "Level 3", "GCS30016", "EAP - Humanities"));
		lectureList.add(new Lecture("영어", "Remedial", "GCS10052", "English Foundation"));
		lectureList.add(new Lecture("영어", "영어 2", "GCS10008", "English Pre-Course 1"));
		lectureList.add(new Lecture("영어", "영어 2", "GCS10009", "English Pre-Course 2"));
		lectureList.add(new Lecture("영어", "영어 2", "GCS20011", "영작문"));
		lectureList.add(new Lecture("영어", "영어 2", "GCS40001", "Essentials of English Communication"));
		lectureList.add(new Lecture("예체능", "스포츠", "GEK10021", "생활체육 1"));
		lectureList.add(new Lecture("예체능", "스포츠", "GEK20021", "생활체육 2"));
		lectureList.add(new Lecture("예체능", "예술", "GEK10061", "음악의 이해"));
		lectureList.add(new Lecture("예체능", "예술", "GEK10062", "미술의 이해"));
		lectureList.add(new Lecture("예체능", "예술", "GEK10065", "영상의 이해"));
		lectureList.add(new Lecture("예체능", "예술", "GEK10098", "교회음악 연습"));
		lectureList.add(new Lecture("외국어", "제 2 외국어", "GCS10019", "독일어 1"));
		lectureList.add(new Lecture("외국어", "제 2 외국어", "GCS10027", "스페인어 1"));
		lectureList.add(new Lecture("외국어", "제 2 외국어", "GCS10033", "일본어 1"));
		lectureList.add(new Lecture("외국어", "제 2 외국어", "GCS10034", "일본어 2"));
		lectureList.add(new Lecture("외국어", "제 2 외국어", "GCS10047", "히브리어 1"));
		lectureList.add(new Lecture("전공기초", "전공기초", "CCC20053", "현대사회와 대중매체"));
		lectureList.add(new Lecture("전공기초", "전공기초", "CCC20072", "영화의 이해"));
		lectureList.add(new Lecture("전공기초", "전공기초", "CSW10003", "심리학개론"));
		lectureList.add(new Lecture("전공기초", "전공기초", "CSW10005", "사회복지개론"));
		lectureList.add(new Lecture("전공기초", "전공기초", "CUE10004", "공간학 입문"));
		lectureList.add(new Lecture("전공기초", "전공기초", "ECE10020", "공학설계 입문"));
		lectureList.add(new Lecture("전공기초", "전공기초", "GEK10108", "공작"));
		lectureList.add(new Lecture("전공기초", "전공기초", "GEK10109", "모두를 위한 인공지능의 활용"));
		lectureList.add(new Lecture("전공기초", "전공기초", "IID10005", "드로잉기초 2"));
		lectureList.add(new Lecture("전공기초", "전공기초", "ISE10002", "언어학개론"));
		lectureList.add(new Lecture("전공기초", "전공기초", "ISE10051", "정치학개론"));
		lectureList.add(new Lecture("전공기초", "전공기초", "ISE10052", "국제관계학입문"));
		lectureList.add(new Lecture("전공기초", "전공기초", "LAW10004", "법학입문"));
		lectureList.add(new Lecture("전공기초", "전공기초", "MEC10001", "경제학 입문"));
		lectureList.add(new Lecture("전공기초", "전공기초", "MEC10002", "경영학 입문"));
		lectureList.add(new Lecture("전공기초", "전공기초", "SIT21003", "글로벌 기업가정신입문"));
		lectureList.add(new Lecture("전공기초", "전공기초", "SIT22002", "ICT 융합입문"));
		lectureList.add(new Lecture("전공기초", "전공기초", "UIL30051", "Survey of American Law"));
		lectureList.add(new Lecture("특론 및 개별연구", "특론", "GEK40091", "교양특론 2"));
		
		setSectionList();
	}
	
	static void setSectionList() {
		lectureList.get(0).addSection("1974673");
		lectureList.get(0).addSection("2049335");
		lectureList.get(1).addSection("1974718");
		lectureList.get(2).addSection("1974720");
	}
}
