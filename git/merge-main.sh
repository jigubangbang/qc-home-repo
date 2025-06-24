#!/bin/bash

# 현재 브랜치 확인
current_branch=$(git rev-parse --abbrev-ref HEAD)

if [[ -n $(git status --porcelain) ]]; then
  echo "커밋되지 않은 변경사항이 존재합니다."
  exit 1
fi

# 'main' 동기화
git checkout main || { echo "⛔️ main 브랜치로 전환할 수 없습니다."; exit 1; }
git pull origin main || { echo "⛔️ main 브랜치를 가져올 수 없습니다."; exit 1; }

# 'main' 브랜치로 체크아웃
git checkout main || { echo "⛔️ main 브랜치로 전환할 수 없습니다."; exit 1; }
git pull origin main || { echo "⛔️ main 브랜치를 가져올 수 없습니다."; exit 1; }

# 작업 브랜치를 'main'에 병합
git merge "$current_branch" -m "Merged $current_branch into main" || { echo "⛔️ $current_branch를 main와와 병합할 수 없습니다."; exit 1; }

# 변경 사항을 원격 저장소에 푸시
git push origin main || { echo "⛔️ main 브랜치를 원격 저장소에 푸시할 수 없습니다."; exit 1; }

# 작업 브랜치로 돌아가기
git checkout "$current_branch" || { echo "⛔️ $current_branch 브랜치로 돌아갈 수 없습니다."; exit 1; }

echo "🎉 작업이 완료되었습니다. 🥳"
