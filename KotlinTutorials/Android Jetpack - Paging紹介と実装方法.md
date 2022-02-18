# Android Jetpack - Paging紹介と実装方法

[Android - Paging 사용 방법](https://codechacha.com/ja/android-jetpack-paging/)

****Android Jetpack - Paging紹介と実装方法****

**[ANDROID](https://codechacha.com/ja/category/android/)[JETPACK](https://codechacha.com/ja/category/android/jetpack/)**

Pagingライブラリは[RecyclerView](https://codechacha.com/ja/android-recyclerview/)にデータをページ単位で効率的にデータをロードして、画面に出力するのに役立ちます。 ページングは、Androidの開発で頻繁に使用されるが、実装する面倒な効率的なリストビューを簡単に実装できるように支援します。

通常のリストビューを作成すると、表示データは多いが、画面に見えるのは一部です。 データをすべてロードしておいて、必要に応じて示している場合、高速良いが、メモリを多く使用します。 一方、必要に応じて動的にロードをすると、メモリを効率的に使用することができます。

![https://codechacha.com/b72459463f6dd62b8abf0f44efa877c8/paging.gif](https://codechacha.com/b72459463f6dd62b8abf0f44efa877c8/paging.gif)

Pagingは効率的です。必要なデータだけをロードして、必要な部分だけUIに表示されます。 初めてリストビューが表示されるときは、いくつかのアイテムだけロードします。そして、ユーザーがスクロールして、より多くのアイテムを表示したいときに追加のデータをロードして表示します。 Pagingを使用する前に、すべての直接実装する必要があったが、Pagingは、このような部分に対して内部的に実装されており、追加で実装する部分がありません。

リストビューに表示されるデータは、Backend(Rest api)で取得することができ、またはローカル(MySQL)からインポートすることができます。 または、 `Backend + Local DB`を一緒に使用することができます。 Backendでデータを受けて、ローカルにキャッシュして、いきなりアクティビティが終了するときに、ローカルからデータをロードすることができます。 キャッシュをすると、ネットワークのコストを削減することができ読み込み速度の利点があります。

Pagingは、[LiveData](https://codechacha.com/ja/android-jetpack-livedata/)、[ViewModel](https://codechacha.com/ja/android-jetpack-viewmodel/)、[Room](https://codechacha.com/ja/android-jetpack-room/)などを使用するように実装なりました。 また、RxJavaをサポートするため、LiveData代わりにRxJavaを使用することもできます。

## **データフロー**

ページングデータを取得しRecylcerViewに示すのは理解しました。 しかし、データをどのように持ってきてどのように出力したいですか？

まず、ページングの次の主要なクラスをまず必要があります。

- **DataSource**：データをロードするオブジェクトです。ローカルまたはBackendのデータを取得役割です。
- **PagedList**：DataSourceから取得したデータはすべてPagedListに転送されます。データの読み込みが必要な場合DataSourceを介して取得します。また、UIにデータを提供する役割をします。
- **PagedListAdapter**：PagedListのデータをRecyclerViewに示すためのRecyclerView.Adapterです。

PagedListはDataSourceを利用して、LocalまたはBackendからデータを取得します。 そのデータは、PagedListAdapterに転送され、RecyclerViewに出力されます。

![https://codechacha.com/static/edd8e789e04a5cd2d2bbdc0a1b8efaff/fcda8/dataflow.png](https://codechacha.com/static/edd8e789e04a5cd2d2bbdc0a1b8efaff/fcda8/dataflow.png)

## **データの読み込み方式**

DataSourceは、ローカルまたはBackendのデータを取得します。 それぞれのデータに応じてロードする方法が異なる場合があります。

次のクラスは、DataSourceの派生クラスであり、それぞれのデータをロードする方法が異なります。

- **PositionalDataSource**：ロケーションベースのデータをロードするDataSourceです。セル数のデータ、固定されたサイズのデータをロードするときに使用されます。

もし終了を知ることができない無限のアイテムであれば、ItemKeyedDataSourceまたはPageKeyedDataSourceが適しています。 RoomはPositionalDataSourceタイプのソースを提供しています。

- **ItemKeyedDataSource**：キーベースのアイテムをロードするDataSourceです。
- **PageKeyedDataSource**：ページベースのアイテムをロードするDataSourceです。

![https://codechacha.com/static/4e47e0d9f5fc6358bd4389b6d7127f0e/fcda8/image03.png](https://codechacha.com/static/4e47e0d9f5fc6358bd4389b6d7127f0e/fcda8/image03.png)

3つのクラスは、すべてDataSourceを継承します。 共通点は、データを持って来るにはあり、違いは、データの塊をインポートすることが違うことです。 簡単に説明します。

### **PageKeyedDataSource**

ページ - キーベースのデータをインポートするときに使用することができます。 もしロードするデータの前のページと次のページのkeyを知ることができればPageKeyedDataSourceを使用することができます。

GitHubはrest apiを提供しており、以下のようにページ単位でクエリを行うことができます。 ページkeyは1から+1ずつ順次増加します。

`https://api.github.com/search/repositories?sort=stars&q=android&page=1`

PageKeyedDataSourceは、以下の3つの関数を実装する必要があります。 この関数だけを実装してくれればPagingこのデータが必要な瞬間に世話を呼び出してデータを取得します。

- **loadInitial**：PagedListが初めてデータをインポートするときに呼び出される関数です。
- **loadAfter**：次のページのデータをロードするときに呼び出されます。
- **loadBefore**：前のページのデータをロードするときに呼び出されます。

最初に `loadInitial`が呼び出されると、最初のkeyを設定し、これに対するデータを取得します。 インポートされたデータは、常に `callback.onResult`にPagingに渡します。 onResultはpreviousPageKeyとnextPageKeyを引数として受け取ります。

次のコードは、PageKeyedDataSourceを実装した例です。 GitHubは、次のページのkeyが順次に増加する数であるため、nextPageKeyを `curPage + 1`に転送しました。

ここで、次のページのデータの読み込みが必要なときに `loadAfter`が呼び出されます。 `loadInitial`で設定したnextPageKeyを引数として渡されます。 このkeyでデータをロードして再nextPageKey、previousPageKeyを設定します。

`loadBefore`は、前のページのデータを読み込むときに呼び出されます。 もしデータがinitから順次に、一方向にロード場合、 `loadBefore`は実装しなくてもされます。

`class RepoDataSource(...) : PageKeyedDataSource<Int, Repo>() {
  ...
    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, Repo>) {
        Log.i(TAG, "Initial Loading, count: ${params.requestedLoadSize}")
        val curPage = 1
        val nextPage = curPage + 1
        searchRepos(service, query, curPage, params.requestedLoadSize, { repos ->
            callback.onResult(repos, null, nextPage)
        }...)
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Repo>) {
        Log.i(TAG, "Loading key: ${params.key}, count: ${params.requestedLoadSize}")
        searchRepos(service, query, params.key, params.requestedLoadSize, { repos ->
            val nextKey = params.key + 1
            callback.onResult(repos, nextKey)
        }...)
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Repo>) {
      ...
    }
}`

上記のコードを実行してみると `loadInitial`で設定したpage keyが`loadAfter`に渡されたことを知ることができます。

`2019-01-06 23:16:57.452 6685-6705/? I/RepoDataSource: Initial Loading, count: 50
2019-01-06 23:17:01.878 6685-6712/? I/RepoDataSource: Loading key: 2, count: 50`

requestedLoadSizeは50であることはPageKeyedDataSourceを作成するときにオプションを50に設定したためです。 以下のようにオプションを設定することができます。

`val pagedListConfig = PagedList.Config.Builder()
    .setPageSize(50)
    .setInitialLoadSizeHint(50) // default: page size * 3
    .setPrefetchDistance(10) // default: page size
    .setEnablePlaceholders(false) // default: true
    .build()`

上の例が含まれているプロジェクトは、[GitHub](https://github.com/codechacha/paging-network)にあります。

### **ItemKeyedDataSource**

Item-keyベースのデータをインポートするときに使用することができます。 このクラスは、PageKeyedDataSourceとほぼ似ています。 違いは、PageKeyedDataSourceは、最初のページを取得するときに、次または前のページのkeyの値をする必要があります。 しかし、ItemKeyedDataSourceは、次のに持って来るkeyを指定しません。 もし最初のデータkeyが `N`であり、次のインポートデータのkeyが`N+1`のようなルールであれば、ItemKeyedDataSourceを適用することができます。

ItemKeyedDataSourceは以下の3つの関数を実装する必要があります。

- **loadInitial**：初めてのデータをインポートするときに呼び出される関数です。
- **loadAfter**：次keyのアイテムをロードするときに呼び出されます。
- **loadBefore**：以前keyのアイテムをロードするときに呼び出されます。

次のコードは、理解を助けるための例です。 実際Rest apiを実装しておらず、 `getWordItems`が仮想的に生成されたデータを返します。 この関数は、引数として渡されたkeyからsize分のデータをbackendからインポートする場合を表現しようとしました。 関数が呼び出されると `key`、`key+1`、...、 `key+size`のクエリの結果をListに入れて返すます。

`class WordDataSource : ItemKeyedDataSource<Int, Word>() {
    ....
    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Word>) {
        Log.i(TAG, "Initial Loading, count: ${params.requestedLoadSize}")
        val initKey = 1
        val items = getWordItems(initKey, params.requestedLoadSize)
        callback.onResult(items)
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Word>) {
        Log.i(TAG, "Loading key: ${params.key + 1}, count: ${params.requestedLoadSize}")
        val items = getWordItems(params.key + 1, params.requestedLoadSize)
        callback.onResult(items)
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Word>) {
    }

    override fun getKey(item: Word): Int {
        return item.getKey()
    }

    fun getWordItems(key: Int, size: Int): List<Word> {
        val list = ArrayList<Word>()
        for (i in 0..(size - 1)) {
            val itemKey = key + i
            list.add(Word("Content of key $itemKey", itemKey))
        }
        return list
    }
}`

このコードを実行してみると、最初のデータをロードする際に `loadInitial`でkeyが1〜20であるデータをロードします。 そしてさらにロードが必要な場合、 `loadAfter`が呼び出され、keyが21〜40であるデータがロードされます。 `loadAfter`の引数で`params.key`は `onResult`に渡された最後のデータのkeyです。 この例では、次のkeyのデータを取得するために `+1`ました。

`2019-01-06 23:11:43.581 6266-6286/? I/WordDataSource: Initial Loading, count: 20
2019-01-06 23:11:43.717 6266-6286/? I/WordDataSource: Loading key: 21, count: 20`

上の例が含まれているプロジェクトは、[GitHub](https://github.com/codechacha/paging-ItemKeyedDataSource)にあります。

### **PositionalDataSource**

名前のように、ロケーションベースのデータに使用することができます。 もし特定の場所(index)で任意の数のデータを取得することができればPositionalDataSourceを適用することができます。 ちなみに、RoomとPagingを一緒に使用する場合RoomはPositionalDataSourceオブジェクトを提供します。

PositionalDataSourceは二つの関数を実装する必要があります。

- **loadInitial**：初めてのデータをインポートするときに呼び出される関数です。
- **loadRange**：次のデータをインポートするときに呼び出されます。

次のコードは、理解を助けるための例です。 ここでも、実際のデータベースを使用せずに範囲のデータを返す `getWordItems`を実装しました。 各関数は、 `startPosition`と`loadSize`を引数として受け取ります。 `callback.onResult`にデータを返します。

`class WordDataSource : PositionalDataSource<Word>() {
    override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<Word>) {
        Log.i(TAG, "Initial Loading, start: ${params.requestedStartPosition}, size: ${params.requestedLoadSize}")
        callback.onResult(
            getWordItems(params.requestedStartPosition, params.requestedLoadSize), 0)
    }

    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<Word>) {
        Log.i(TAG, "Initial Loading, start: ${params.startPosition}, size: ${params.loadSize}")
        callback.onResult(
            getWordItems(params.startPosition, params.loadSize))
    }

    fun getWordItems(startPosition: Int, loadSize: Int): List<Word> {
        val list = ArrayList<Word>()
        for (i in 0..(loadSize - 1)) {
            val itemPos = startPosition + i
            list.add(Word("Content position $itemPos", itemPos))
        }
        return list
    }
}`

ログを確認してみると、 `loadInitial`で最初の20個のデータを取得し、必要に応じて`loadRange`で追加のデータを取得しました。

`2019-01-07 21:12:57.209 4862-4881/com.sample.basicsample I/WordDataSource: Initial Loading, start: 0, size: 20
2019-01-07 21:12:57.302 4862-4881/com.sample.basicsample I/WordDataSource: Loading, start: 20, size: 20
2019-01-07 21:12:59.261 4862-4883/com.sample.basicsample I/WordDataSource: Loading, start: 40, size: 20`

上の例が含まれているプロジェクトは、[GitHub](https://github.com/codechacha/paging-PositionalDataSource)にあります。

## **PagedListはどのように生成されるか**

PagedListとPagedListAdapterの関係を簡単に説明すると、PagedListはDataSourceを使用して、データを取得し、PagedListAdapterに転送します。

構造的に説明すると、PagedListAdapterはPagedListデータのスナップショットとして認識します。 スナップショットが写り込むのデータを画面に表示します。 もしデータベースにデータが追加される場合には、PagedListも新たに生成する必要があります。 PagedListAdapterは、新たに変更されたPagedListのデータを画面に出力できます。

このようなPagedListはどのように生成できますか？まず、次の主要なクラスを知っている。

- **DataSource.Factory**：DataSourceを生成する役割をします。
- **LivePagedListBuilder**：PagedListを生成するビルダーです。ビルダーはLiveData に戻します。

PagedListは、データをロードするために、内部フィールドにDataSourceオブジェクトを持っています。 そのため、LivePagedListBuilderがPagedListを作成するには、DataSource.Factoryが必要です。

次のコードを見れば、LivePagedListBuilderはDataSource.Factoryを引数として受けLiveData を生成します。

`val dataSourceFactory = RepoDataFactory(query, service)
val data: LiveData<PageList<Repo>> = LivePagedListBuilder(dataSourceFactory, pagedListConfig)
    .build()`

`LivePagedListBuilder.java`コードを見ると、LivePagedListBuilderはDataSource.FactoryにDataSourceを作成します。 DataSourceを作成した後は、PagedList.BuilderでPagedListを生成します。

`public final class LivePagedListBuilder {
  public LiveData<PagedList<Value>> build() {
    ...
    mDataSource = dataSourceFactory.create();
    mDataSource.addInvalidatedCallback(mCallback);

    mList = new PagedList.Builder<>(mDataSource, config)
    ...
  }
}`

多くのファクトリーとビルダーの助けを借りてDataSourceを持っているPagedListが生成されました。

## **PagedListAdapterはどのようにデータを渡す受けるか**

PagedListAdapterはPagedListデータのスナップショットとして認識します。 PagedListのデータをRecyclerViewに出力します。

初期化の過程でPagedListはDataSourceにアイテムをロードします。 その後にRecyclerView.AdapterのnotifyItemRangeInsertedをコールバックして、データが追加されたことを通知します。 その後、Adapterは、RecyclerViewにデータを出力します。

ユーザーがスクロールして最後のアイテムが見える場合には、PagedListは分かってDataSourceに次の表示項目をロードします。 アイテムが追加されると、notifyItemRangeInsertedがPagedListAdapterにデータを追加を通知します。

例として作成されたPagedListAdapterを継承するReposAdapterです。

`class ReposAdapter : PagedListAdapter<Repo, RecyclerView.ViewHolder>(REPO_COMPARATOR) {
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
      return RepoViewHolder.create(parent)
  }

  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
      val repoItem = getItem(position)
      if (repoItem != null) {
          (holder as RepoViewHolder).bind(repoItem)
      }
  }
  ....
}`

`adapter.submitList`にPagedList をPagedListAdapterに伝達してくれることができます。

`viewModel.repos.observe(this, Observer<PagedList<Repo>> {
    adapter.submitList(it)
})`

`adapter.submitList`のコードに沿って行ってみればPagedListはDataSourceにデータを取得し、PagedListAdapterにデータが追加されたことを示します。

## **プレースホルダー**

Placeholdersは、データがロードされず、画面に表示されないとき、仮想のオブジェクトを事前にそしてデータの読み込みが完了したとき、実際のデータを示すことを意味します。

![https://codechacha.com/static/1a3336257cb994453f3771752384a2f2/9cb4e/placeholder.png](https://codechacha.com/static/1a3336257cb994453f3771752384a2f2/9cb4e/placeholder.png)

PagedListを作成するとき、オプションでPlaceholdersを使用するかを選択することができます。

`val pagedListConfig = PagedList.Config.Builder()
    .setEnablePlaceholders(true)
    .build()

val data = LivePagedListBuilder(dataSourceFactory, pagedListConfig)`

Placeholdersは次のような利点があります。 *すばやくスクロールすることができる *スクロールバーの位置が正確である *スピナーなどで**その他**のような機能を作成する必要がない

スクロールが切れないため、ユーザーが探しているものをすばやく見つけることができます。 そして、実際のデータが画面に見られ、もスクロールの位置が変更されない利点がのです。

Placeholdersを使用するには、次のような条件を満たしている必要があります。

- アイテムが表示されるViewのサイズが同じでなければならない
- Adapterがnullを処理しなければならない
- DataSourceで提供されるアイテムの数が決まってなければならない

Adapterにnullが入ってくるので、nullが来たときUIなどの処理をしてくれるとします。 実際のデータが出力されるPlaceholdersのサイズと異なっている場合の位置が少しずつ異なります。 だからViewのサイズを同じにする必要があります。

## **RxJavaサポート**

PagingはLiveDataだけでなく、RxJavaもサポートします。 LiveData代わりにObservableを使用するには、Builderクラスを変更する必要があります。

`val data: LiveData<PagedList<Item>> =
    LivePagedListBuilder(dataSourceFactory, config)
    .build()`

`RxPagedListBuilder`は`Observable<PagedList>`形式のオブジェクトを返します。

`val data: Observable<PagedList<Item>> =
    RxPagedListBuilder(dataSourceFactory, config)
    .buildObservable()`

## **まとめ**

Pagingについて簡単に調べてみました。 次の記事では、Pagingをどのように適用するかのサンプルを作って見て調べます。