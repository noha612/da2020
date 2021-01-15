"#da2020"

# koolj_dataengineering

# experience

Vài trial nhỏ để anh chị em thử một map tra cứu nhanh trên địa bàn Hà Nội. Anh chị e có thể tra trên
12 quận huyện nội ngoại thành, tất cả các ngõ ngách số nhà anh chị em cần tìm.

Demo: dathoc.net/demo Phần DE, xem Implementing Spark, Redis, Bot NLP để tìm đường
(các bạn dùng nhẹ nhàng nhé, ko hệ thống demo sập)

-----------------
Vậy qua đây cũng share để anh chị em làm DA biết là: Em phải học những ngôn ngữ lập trình gì và kiến
trúc gì để làm được ứng dụng thế này. Đơn giản thôi ạ...

👉1. FrontEnd Hệ thống này cần Javascript, CSS, HTML cho frontend Hệ thống cần tích hợp socket để
gửi nhận từ server qua client Hệ thống cần tích hợp bên thứ 3 để view dữ liệu bản đồ hành chính:
sông núi đường phố - geographical map asset title. Vậy việc tự dựng server title hoặc lấy bên thứ ba
Sau đó lên thần với việc master bootstrap Nói một cách đúng hơn, nếu bạn thành thạo
bootstrap/vue/angular/react js... hoặc một cái tương tự, điều đó có nghĩa bạn không cần team Design
để thiết kế cho ứng dụng của bạn nữa.

👉1b. Dựng server title map Cái này OpenStreetMap có cho miễn phí xong cần cấu hình hơi đầu lâu, và
rắc rối. Vận dụng lấy lại tài nguyên các bạn VMap, ArcGis hoặc Here hoặc Grab Map, Việt Map là tốt
nhất. Cách lấy rất đơn giản:

+ Khẩu độ zoom và mỗi khẩu độ zoom
+ Lưu lại ảnh hình vuông của map title là xong
+ Sau đó xem doc dựng server map title của openstreet map

👉1c. Dựng latlong location và địa chỉ Cái này các bên nhiều lắm, chỉ sợ không có sức mà lấy thôi.
Mình thử trầy trật java multithread qua mấy bạn Vmap, Grab, Be, GoViet thì.. nói chung các bạn ý
cũng có địa chỉ chính xác. 20 phút bạn crawl thì lấy đc cỡ 400k tới 1 triệu điểm address: gồm tên
các địa chỉ, số nhà, tên quán, tên đường, phường, quận... Có những cái phải cần fans đi xung quanh
để xác định địa chỉ đúng hay sai: ví dụ: tất cả các map đều không có đường tên là... Yên Phụ Nhỏ.
Chúng ta đều quen với con đường đó, xong các map ko có. Vậy phải định nghĩa cho map của mình. Tiếp
tới là clean cai latlong để insert vào Redis cho tra cứu nhanh. Code clean nếu làm từ đầu có thể bạn
mất cỡ 2 tháng, xong khi khi đã có quy trình thì chỉ 10 phút bạn có một địa phương hành chính để tra
cứu. Tiếp tới là viết các api tìm đường, tìm điểm lân cận, tìm route.. cái này redis hay postgres hỗ
trợ nguyên thuật toán. Bạn không cần sáng tác gì thêm

👉2. BackEnd Cái này thì cần NodeJS và các package của nó: Express, CouchDB... Cái này cũng cần học
cẩn thận khi kết nối các package khác của nodejs Hiểu khi kết nối database Học tiếp về database cơ
bản và cách tạo bảng, ra vào với NodeJS

👉2b. Dựng server Redis Đơn giản bạn muốn inmemory tra cứu data Geo nhanh thì cấu hình bạn này lên.
Server tạo đơn giản lắm, Google Redis setup ra hết. Nhưng vì sao phải dùng cái này trong khi có
CouchDB rồi. Vì cái này Redis có phần Geo, nó quản trị luôn cho bạn việc tra cứu địa chỉ và tìm
đường ngắn nhất cho bạn. Tất nhiên nếu bạn ko dùng thì dùng Posgres, package PostGis, tương tự như
Redis, xong ko nhanh bằng Redis Cái còn lại là bạn tạo lucene index để cho thuật toán tối ưu nhất
như bạn tra đường như tôi làm trong demo Tích hợp luôn thiết kế dạng hàng quán, địa chỉ hay tới lưu
luôn lên bạn này.

👉2c. Dựng bot nlp cho tra cứu chuyển hướng nhanh Bạn có thể dùng bất kỳ loại bot nào: Dialog flow,
Rasa, AWS lex... bất cứ cái gì để luyện cho nó những cái này Đầu vào số -> nó phải vào json trường
data số nhà Đầu vào tên bất kỳ (đường, ngõ, phường, quận) -> nó phải tự động khép vào trường data
tương ứng Như thế việc query tiếp sẽ tối ưu.

👉3. NLP modeling Chuyên môn cho dân làm mô hình nhận diện trí tuệ nhân tạo qua Text. Bạn cần học
RNN, lstm... và kết hợp với tổ hợp thư viện spacy cho Tiếng Việt, để con bot trên nó nhận diện nhanh
TV. Vì mình làm TV nên cần TV, bạn làm tiếng Thổ thì chuyển sang tiếng Thổ

👉4. Operation on Linux Đúng rồi 90% các ứng dụng trên làm trên linux là tiện nhất. Và dễ cấu hình.
Còn nếu bạn cứ cố vào Windows thì.. có thể ko mấy tutor trên mạng hỗ trợ bạn. Vậy cần học ngay các
cú pháp lệnh cơ bản trên linux. Và dần dà có thể thay không dùng Win nữa mà sang Ubuntu 20 cho lành.

👉5. Ethics in IT Ơ...tại sao phải học cái này, đơn giản là 90% ai làm DA cũng sẽ có xu hướng là
những tên TRỘM ĐẠO. Vậy cần hiểu rõ về Đạo Đức trong Công nghệ thông tin. Anh chị e cứ search trên
Youtube nhiều lắm. Học để đi theo bụt, chứ ko nên theo ma.

👉6. Negotiate customer Cái này cần này. Vì cuối cùng mình phải làm một cái gì đó cho khách hàng
hiểu, vậy: Cách đàm phán, thương thuyết với khách hàng nói chung, và khách hàng nói Tiếng Anh nói
riêng là ...bắt buộc. Học thêm tiếng Anh để nói chuyện được với khách hàng

👉7. Documenting Cái này RẤT CẦN vì bạn cần có khả năng trình bày và mô tả lại những cái bạn làm cho
nhiều đám đông hiểu. Cần học để mô tả theo phương cách: Ai nhìn vào, team nào nhìn vào (thuộc CNTT)
cũng sẽ hiểu, dù các bạn ý la BA, hay TESTER hay DEV hay Tổng Giám Đốc. Dạy ở đâu, bạn cứ lên
Youtube search từ khoá: How to documenting, hay là: How to write a specification là ra nhiều mẫu
lắm.

Gluk!
