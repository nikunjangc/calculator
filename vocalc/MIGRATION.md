# Moving vocalc to its own repo (`nikunjangc/abacus`)

The vocalc app currently lives in the `vocalc/` subfolder of the `calculator`
repo, on branch `claude/vocalc-spinoff` (PR #6). Here's how to promote it to the
standalone `abacus` repo at root level.

Pick **one** of the two options below.

---

## Option A — Preserve git history (recommended)

`git subtree split` extracts just the `vocalc/` folder into its own branch with
the relevant commit history, then pushes it to `abacus`.

```bash
# 1. Get the branch
git clone https://github.com/nikunjangc/calculator.git
cd calculator
git checkout claude/vocalc-spinoff

# 2. Split the subfolder into a history-preserving branch
git subtree split --prefix=vocalc -b vocalc-root

# 3. Push that branch to abacus as main
git push https://github.com/nikunjangc/abacus.git vocalc-root:main
```

If `abacus` was created **with** an initial commit (e.g. an auto-generated
README) the push in step 3 will be rejected as non-fast-forward. Either force it
(safe on a brand-new repo):

```bash
git push --force https://github.com/nikunjangc/abacus.git vocalc-root:main
```

…or create `abacus` empty (no README/.gitignore/license) so step 3 is a clean
first push.

---

## Option B — Simple copy (no history)

Fastest if you don't care about preserving the handful of scaffold commits.

```bash
# 1. Clone both
git clone https://github.com/nikunjangc/abacus.git
git clone --branch claude/vocalc-spinoff https://github.com/nikunjangc/calculator.git

# 2. Copy the app contents to abacus root (note the trailing /. )
cp -r calculator/vocalc/. abacus/

# 3. Commit and push
cd abacus
git add .
git commit -m "Import vocalc: voice-first cross-platform calculator"
git push origin main
```

---

## After it's on `abacus`

```bash
flutter create .            # generate android/ios/web/desktop platform folders
flutter pub get
flutter test
flutter run --dart-define=ANTHROPIC_API_KEY=sk-ant-...
```

Then add the microphone/speech permissions documented in `README.md`
(Android `AndroidManifest.xml` + iOS `Info.plist`).

---

## Letting Claude do it for you

If you start a Claude Code web session with **both** `calculator` and `abacus`
in scope, just say:

> Copy the `vocalc/` folder from the `claude/vocalc-spinoff` branch of
> `calculator` into the root of `abacus` and push (see `vocalc/MIGRATION.md`).

and it will run Option A end-to-end.
