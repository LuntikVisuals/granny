# Granny

**Granny** — мобильный 2D хоррор-побег для телефона (вид сверху) из дома безумной бабушки. Цель — выжить 5 дней, собрать предметы и сбежать, не производя шума.

Есть выбор персонажа (выжившие + антагонисты Бабушка/Дед), рейтинг и локальный мультиплеер по Wi-Fi.

Вся игра — один `index.html` (canvas). Android-версия — нативная WebView-обёртка, собирается в APK точно так же, как Windup Wonderland.

## Сборка APK (как в Windup)

1. Скопировать `index.html` в `android/app/src/main/assets/index.html`
2. Иметь keystore в `/workspace/apk-keys/keystore.properties` (как в других проектах)
3. В папке `android/` запустить:
   ```
   ./gradlew assembleRelease
   ```
4. APK лежит в `android/app/build/outputs/apk/release/`
5. Для публикации — залить base64 в `.github/bootstrap/` и пушнуть (workflow создаст Release)

Версия Android: versionCode 1 / versionName 1.0. Тот же ключ — ставится поверх предыдущих.

## Играть

- В браузере: открой `index.html` (лучше с телефона в альбомной)
- APK: собрать как выше и поставить

## Текущий статус

Прототип: движение, комнаты, Бабушка слышит шум, выбор персонажа (Бабушка/Дед как антагонисты), рейтинг-заглушка, LAN-скелет.

Структура один-в-один с Windup Wonderland.
