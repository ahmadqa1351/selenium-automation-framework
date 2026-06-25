# CI/CD Integration Guide - Selenium Automation Framework

## 🔄 Continuous Integration Setup

### GitHub Actions

**Workflow File**: `.github/workflows/selenium-tests.yml`

#### Features:
- Runs on push to main/develop branches
- Triggers on pull requests
- Scheduled daily runs
- Manual workflow dispatch
- Multi-OS and multi-Java testing
- Artifact upload and reporting
- PR comments with results
- Slack notifications

#### Running Tests via GitHub Actions:

1. **On Push**
   ```bash
   git push origin main
   # Automatically triggers workflow
   ```

2. **On Pull Request**
   ```bash
   git push origin feature-branch
   # Create PR to main
   # Workflow runs automatically
   ```

3. **Manual Trigger**
   - Go to GitHub repo
   - Actions tab → Select workflow
   - "Run workflow" button
   - Select branch and inputs

4. **View Results**
   - Go to Actions tab
   - Click workflow run
   - View logs and artifacts
   - Download reports

### Jenkins

**Pipeline File**: `Jenkinsfile`

#### Setup:

1. **Install Jenkins**
   ```bash
   # Docker
   docker run -d -p 8080:8080 jenkins/jenkins:latest
   
   # Or download from jenkins.io
   ```

2. **Configure Job**
   - New Item → Pipeline
   - Name: "Selenium Automation Tests"
   - Definition: Pipeline script from SCM
   - SCM: Git
   - Repository URL: Your GitHub repo
   - Script Path: `Jenkinsfile`

3. **Configure Credentials**
   - Jenkins → Credentials → System → Global
   - Add credentials for Git

4. **Set Build Parameters**
   - ENVIRONMENT: qa, uat, prod
   - BROWSER: chrome, firefox, edge
   - TEST_SUITE: smoke, regression, e2e, all

#### Running Tests:

```bash
# Via Jenkins UI
1. Open Jenkins dashboard
2. Click job "Selenium Automation Tests"
3. Click "Build with Parameters"
4. Select parameters
5. Click "Build"

# View results in console output
```

### Azure DevOps

**Pipeline File**: `azure-pipelines.yml`

#### Setup:

1. **Create Project**
   - Go to dev.azure.com
   - Create new project

2. **Create Pipeline**
   - Pipelines → Create Pipeline
   - Select repository
   - Configure pipeline
   - Select existing YAML file: `azure-pipelines.yml`
   - Save and queue

3. **Configure Variables**
   - Pipeline settings
   - Set variables:
     - ENVIRONMENT
     - BROWSER
     - PARALLEL_THREADS

#### Running Tests:

```
1. Go to Pipelines
2. Select pipeline
3. Click "Run pipeline"
4. Select branch
5. Click "Run"
6. View results in pipeline runs
```

## 🐳 Docker Execution

### Build Docker Image

```bash
# Build image
docker build -t selenium-automation:latest docker/

# Build with tag
docker build -t selenium-automation:1.0 docker/

# Build and push to registry
docker build -t registry.example.com/selenium-automation:latest docker/
docker push registry.example.com/selenium-automation:latest
```

### Run Tests in Docker

```bash
# Run with default settings
docker run --rm selenium-automation:latest

# Run with environment variables
docker run --rm \
  -e ENVIRONMENT=qa \
  -e BROWSER=chrome \
  -e PARALLEL_THREADS=4 \
  selenium-automation:latest

# Run with volume mount for reports
docker run --rm \
  -v $(pwd)/reports:/app/reports \
  -v $(pwd)/logs:/app/logs \
  selenium-automation:latest
```

### Docker Compose

```bash
# Start services
docker-compose -f docker/docker-compose.yml up

# With environment variables
ENVIRONMENT=qa BROWSER=chrome docker-compose -f docker/docker-compose.yml up

# Run in background
docker-compose -f docker/docker-compose.yml up -d

# Stop services
docker-compose -f docker/docker-compose.yml down

# View logs
docker-compose -f docker/docker-compose.yml logs -f
```

## 📊 Test Reporting

### Extent Reports

Generated automatically after test execution:

```
reports/extent-reports/TestExecutionReport_YYYY-MM-DD_HH-mm-ss.html
```

**Features:**
- Interactive dashboard
- Test summary
- Detailed logs
- Screenshots for failures
- System information
- Timeline view

### Publishing Reports in CI/CD

#### GitHub Actions
```yaml
- name: Upload Test Reports
  uses: actions/upload-artifact@v3
  with:
    name: extent-reports
    path: reports/extent-reports/
```

#### Jenkins
```groovy
publishHTML([
    reportDir: 'reports/extent-reports',
    reportFiles: 'index.html',
    reportName: 'Extent Test Report'
])
```

#### Azure DevOps
```yaml
- task: PublishBuildArtifacts@1
  inputs:
    PathtoPublish: '$(Build.ArtifactStagingDirectory)'
    ArtifactName: 'test-artifacts'
```

## 🔔 Notifications

### Email Notifications

#### Jenkins
```groovy
emailext(
    subject: "Test Execution ${BUILD_STATUS}",
    body: "Build ${BUILD_URL} finished.",
    to: 'qa-team@example.com'
)
```

#### Azure DevOps
```yaml
- task: SendEmail@1
  inputs:
    SendEmailTo: 'qa-team@example.com'
    EmailSubject: 'Test Execution Results'
    EmailBody: 'Check pipeline for details'
```

### Slack Notifications

#### GitHub Actions
```yaml
- name: Slack Notification
  uses: slackapi/slack-github-action@v1
  with:
    payload: |
      {
        "text": "Test Results: SUCCESS"
      }
  env:
    SLACK_WEBHOOK_URL: ${{ secrets.SLACK_WEBHOOK_URL }}
```

## 🔐 Secrets Management

### GitHub Actions
```
Settings → Secrets → New repository secret
```

**Common Secrets:**
- `SLACK_WEBHOOK_URL` - Slack notifications
- `DB_PASSWORD` - Database credentials
- `API_KEY` - API authentication

### Jenkins
```groovy
withEnv(['SLACK_WEBHOOK_URL=credentials("slack-webhook")']) {
    // Use secret
}
```

### Azure DevOps
```yaml
variables:
  - group: 'Secrets'  # Link variable group
```

## 🚀 Deployment Pipeline

### Example: Complete CI/CD Pipeline

```yaml
stages:
  - Build
    - Compile code
    - Run unit tests
    - Build artifact
  
  - Test
    - Run smoke tests
    - Run regression tests
    - Generate reports
  
  - Report
    - Publish test results
    - Upload artifacts
    - Send notifications
  
  - Deploy (optional)
    - Deploy to staging
    - Run E2E tests
    - Deploy to production
```

## ✅ Checklist for CI/CD Setup

- [ ] Pipeline file created (GitHub Actions/Jenkins/Azure)
- [ ] Repository configured
- [ ] Credentials configured
- [ ] Environment variables set
- [ ] Test data accessible
- [ ] Reports location configured
- [ ] Notifications set up
- [ ] Secrets stored securely
- [ ] Pipeline tested successfully
- [ ] Documentation updated

## 🐛 Troubleshooting CI/CD

### Tests Timeout in Pipeline
```bash
# Increase timeout
# config.properties
explicit.wait.timeout=20
page.load.timeout=30
```

### Out of Memory
```yaml
# Increase JVM memory
MVEN_OPTS: '-Xmx2048m'
```

### WebDriver Issues
```bash
# Ensure headless mode
headless=true
```

### Report Not Generated
```bash
# Check report path permissions
# Verify Extent Reports configuration
```

---

**CI/CD Best Practices:**
- ✅ Run tests on every commit
- ✅ Generate detailed reports
- ✅ Archive artifacts
- ✅ Send notifications
- ✅ Monitor pipeline metrics
- ✅ Use parallel execution
- ✅ Implement retry logic
- ✅ Clean up old artifacts
