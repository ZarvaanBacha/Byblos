:: https://firebase.google.com/docs/emulator-suite/connect_rtdb#import_and_export_data
@echo off
firebase emulators:start --import=./dump --export-on-exit
@echo on

