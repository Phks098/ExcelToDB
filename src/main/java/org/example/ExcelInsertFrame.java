package org.example;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * 엑셀 데이터를 불러와 선택한 열을 제외하고 DB에 Insert 하기 위한 GUI 화면
 * (화면 구성만 포함, 실제 로직은 TODO 위치에 구현)
 *
 * @author Jusung
 * @since 2026-10-03
 */
public class ExcelInsertFrame extends JFrame {

    // ── 상단: 파일 선택 ──
    private final JTextField filePathField = new JTextField();
    private final JButton fileSelectButton = new JButton("파일 선택");
    private final JButton loadButton = new JButton("불러오기");

    // ── 중앙: 엑셀 미리보기 테이블 ──
    private final DefaultTableModel tableModel = new DefaultTableModel();
    private final JTable previewTable = new JTable(tableModel);

    // ── 우측: 제외할 열 선택 ──
    private final JPanel columnCheckPanel = new JPanel();

    // ── 하단: 테이블명 입력 + Insert ──
    private final JTextField tableNameField = new JTextField(20);
    private final JButton insertButton = new JButton("Insert");
    private final JLabel statusLabel = new JLabel("엑셀 파일을 선택하세요.");

    public ExcelInsertFrame() {
        setTitle("Excel → DB Insert");
        setSize(1000, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel root = new JPanel(new BorderLayout(10, 10));
        root.setBorder(new EmptyBorder(10, 10, 10, 10));
        root.add(createTopPanel(), BorderLayout.NORTH);
        root.add(createCenterPanel(), BorderLayout.CENTER);
        root.add(createRightPanel(), BorderLayout.EAST);
        root.add(createBottomPanel(), BorderLayout.SOUTH);
        setContentPane(root);

        bindEvents();
        showSampleColumns(); // 화면 확인용 샘플 (실제 로직 연결 후 삭제)
    }

    /** 상단: 파일 경로 + 파일 선택 / 불러오기 버튼 */
    private JPanel createTopPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(new TitledBorder("엑셀 파일"));

        filePathField.setEditable(false);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        buttons.add(fileSelectButton);
        buttons.add(loadButton);

        panel.add(filePathField, BorderLayout.CENTER);
        panel.add(buttons, BorderLayout.EAST);
        return panel;
    }

    /** 중앙: 엑셀 데이터 미리보기 */
    private JScrollPane createCenterPanel() {
        previewTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF); // 열이 많을 때 가로 스크롤
        previewTable.getTableHeader().setReorderingAllowed(false);

        JScrollPane scrollPane = new JScrollPane(previewTable);
        scrollPane.setBorder(new TitledBorder("데이터 미리보기"));
        return scrollPane;
    }

    /** 우측: 제외할 열 체크박스 목록 */
    private JScrollPane createRightPanel() {
        columnCheckPanel.setLayout(new BoxLayout(columnCheckPanel, BoxLayout.Y_AXIS));

        JScrollPane scrollPane = new JScrollPane(columnCheckPanel);
        scrollPane.setBorder(new TitledBorder("제외할 열 선택"));
        scrollPane.setPreferredSize(new Dimension(200, 0));
        return scrollPane;
    }

    /** 하단: 테이블명 입력 + Insert 버튼 + 상태 표시 */
    private JPanel createBottomPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));

        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        inputPanel.add(new JLabel("테이블명"));
        inputPanel.add(tableNameField);
        inputPanel.add(insertButton);

        panel.add(statusLabel, BorderLayout.WEST);
        panel.add(inputPanel, BorderLayout.EAST);
        return panel;
    }

    /** 버튼 이벤트 연결 */
    private void bindEvents() {
        fileSelectButton.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                    "Excel 파일 (*.xlsx, *.xls)", "xlsx", "xls"));

            if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                filePathField.setText(chooser.getSelectedFile().getAbsolutePath());
                statusLabel.setText("파일이 선택되었습니다. [불러오기]를 누르세요.");
            }
        });

        loadButton.addActionListener(e -> {
            // TODO(Jusung): POI로 엑셀 읽기 → tableModel에 헤더/데이터 세팅 → setColumnCheckBoxes(헤더) 호출
            statusLabel.setText("불러오기 로직 미구현");
        });

        insertButton.addActionListener(e -> {
            // TODO(Jusung): 체크되지 않은 열만 골라 tableNameField 테이블에 INSERT
            statusLabel.setText("Insert 로직 미구현");
        });
    }

    /** 엑셀 헤더를 받아 '제외할 열' 체크박스를 다시 그림 */
    public void setColumnCheckBoxes(String[] headers) {
        columnCheckPanel.removeAll();
        for (String header : headers) {
            columnCheckPanel.add(new JCheckBox(header));
        }
        columnCheckPanel.revalidate();
        columnCheckPanel.repaint();
    }

    /** 화면 확인용 샘플 데이터 (로직 연결 후 삭제) */
    private void showSampleColumns() {
        String[] headers = {"번호", "이름", "학과", "프로그램명", "참여일"};
        tableModel.setColumnIdentifiers(headers);
        tableModel.addRow(new Object[]{"1", "홍길동", "컴퓨터공학과", "취업 특강", "2026-03-02"});
        tableModel.addRow(new Object[]{"2", "김철수", "경영학과", "모의 면접", "2026-03-05"});
        setColumnCheckBoxes(headers);
    }

    // 이벤트 등록 통로
    public void addLoadListener(ActionListener l)   { loadButton.addActionListener(l); }
    public void addInsertListener(ActionListener l) { insertButton.addActionListener(l); }

    // Controller가 쓰는 값 조회/화면 갱신 메서드
    public String getFilePath()  { return filePathField.getText(); }
    public String getTableName() { return tableNameField.getText().trim(); }
    public void showStatus(String msg)   { statusLabel.setText(msg); }

}