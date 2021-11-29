# Compiler
University Project

در فاز اول پروژه سعی بر بررسی کلمات، اپراتورها، کلمات کلیدی، آیدی ها و... را داشتیم. هدف این بود تا به برنامه یک قطعه کد به عنوان
ورودی داده شود و برنامه با بررسی جزئیات برنامه دنباله ای از توکن های بدست امده را به خروجی تحویل دهد. پس به عنوان مدل بندی
ابتدا NFA مسیر رسم و کلمات کلیدی و اپراتورها شناسایی و نقشه راه مشخص شد. در مرحله بعد NFA به DFA جهت سهولت در پیاده
سازی تبدیل گردید و DFA حاصل پیاده سازی شد.
در ابتدا نیاز هست تا کلمات کلیدی (KeyWords) و همچنین اپراتورها (operators) بصورت پیش فرض تعریف شوند و همچنین
درجهت بهبود سرعت کامپایلر تعریف آنان بصورت هشینگ صورت گیرد. جدول نمار نیز بصورت hashMap پیاده سازی گردید تا بصورت
کلید-مقدار با عنوان نام آیدی-نوع آیدی مقادیر ذخیره گردند. به متدهایی نیاز هست تا چک کنند آیا توکن مورد بررسی کلمه کلیدی
هست (isKeyWord) یا یا اپراتور هست (isOperator) یا عدد ساده (isNumber) یا اگر چنین نبود آیدی ساده. همچنین به متدهایی
جهت پیمایش whitespace ها نیاز داشتیم و همچنین به متدهایی جهت تبادل با جدول نمار نیاز بود , saddToSymbol()lisSymbo, lgetSymbo
برنامه بدینصورت عمل میکند که تازمانی که توکنی برای خواندن وجود داشته باشد ادامه میدهد و خط به خط ورودی ها را بررسی میکند.
برای این کار هر خط به ارایه ای از کاراکتر ها تبدیل میشوند و تحویل متد readNextWord داده میشوند تا توکن ها را از هم تفکیک
کند و آن )کلمه یا هر چیز دیگر( را برگرداند.
در آخر با چک کردن word تفکیک شده با متدهای از پیش تعریف شده نوع آن نیز مشخص میشود و اقدام به چاپ توکن به همراه نوع
آن میشود.

DFA:

![DFA](https://s4.uupload.ir/files/screenshot_2021-11-29_170022_4q1d.jpg)
