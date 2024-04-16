from google.oauth2.service_account import Credentials
from googleapiclient.discovery import build
from googleapiclient.http import MediaIoBaseDownload
import io
import os


# The path to your service account key file
SERVICE_ACCOUNT_FILE = '/tmp/gcloud.json'  # Or the path where you saved the credentials in your CI/CD environment

# The ID of the file you want to download from Google Drive
FILE_IDS = ['1Ok-54kVJGkFfU0F8JwHZ0ZgtgJtNpmIy', '1iitulF-befScIR51kQu_FZpM4Nu3Ast3', '1AJGBrXprWlDmeXOpH_cBE4gs2ZLuKwEO']


def download_file(file_id, output_directory):
    # Authenticate and construct service
    credentials = Credentials.from_service_account_file(
            SERVICE_ACCOUNT_FILE, scopes=['https://www.googleapis.com/auth/drive'])
    service = build('drive', 'v3', credentials=credentials)

    # Request to download the file
    request = service.files().get_media(fileId=file_id)
    fh = io.BytesIO()
    downloader = MediaIoBaseDownload(fh, request)
    done = False

    while not done:
        status, done = downloader.next_chunk()
        print(f"Download of file ID {file_id} is {int(status.progress() * 100)}% complete.")

    # Ensure the output directory exists
    if not os.path.exists(output_directory):
        os.makedirs(output_directory)

    # Get the file name from the Drive metadata
    file_metadata = service.files().get(fileId=file_id, fields='name').execute()
    file_name = file_metadata.get('name', 'downloaded_file')

    # Write the downloaded content to a file
    output_file_path = os.path.join(output_directory, file_name)
    with io.open(output_file_path, 'wb') as f:
        fh.seek(0)
        f.write(fh.read())
    
    return file_name

if __name__ == '__main__':
    output_directory = './src/resources/downloaded_files'
    absolute_path = os.path.abspath(output_directory)
    print(f"The absolute path to the downloaded files is: {absolute_path}")
    for file_id in FILE_IDS:
        file_name = download_file(file_id, output_directory)
        print(f'File {file_name} has been downloaded and saved in {output_directory}')
    
 